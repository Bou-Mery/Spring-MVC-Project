package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Option;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.respository.OptionRepository;

import com.opencsv.CSVReader;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/options")
public class OptionController {

    private final OptionRepository optionRepository;

    public OptionController(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @GetMapping
    public String listOptions(Model model , HttpSession httpSession) {
        model.addAttribute("options", optionRepository.findAll());
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne
        return "options";
    }

    @PostMapping("/add")
    public String addOption(@ModelAttribute Option newOption) {
        // Vérifier si l'option existe déjà dans la base de données
        if (optionRepository.existsByName(newOption.getName())) {
            // Si l'option existe déjà, rediriger avec un message d'erreur
            return "redirect:/options?error=duplicate"; // Vous pouvez passer un paramètre pour gérer le message d'erreur
        }

        // Si l'option est unique, l'ajouter à la base de données
        optionRepository.save(newOption);
        return "redirect:/options"; // Rediriger vers la liste des options
    }

    @PostMapping("/edit")
    public String editOption(@ModelAttribute Option editedOption) {
        optionRepository.save(editedOption);
        return "redirect:/options";
    }

    @GetMapping("/delete/{id}")
    public String deleteOption(@PathVariable int id) {
        optionRepository.deleteById(id);
        return "redirect:/options";
    }

    // File upload handler for CSV and XLSX
    @PostMapping("/import")
    public String importOptions(@RequestParam("file") MultipartFile file) {
        try {
            // Determine file type and process accordingly
            if (file.getOriginalFilename().endsWith(".csv")) {
                importFromCSV(file);
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                importFromXLSX(file);
            } else {
                throw new IllegalArgumentException("Invalid file type. Please upload CSV or XLSX.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions, e.g., invalid file format
            return "redirect:/options?error=1"; // Show error on failure
        }
        return "redirect:/options"; // Successfully imported, redirect to options list
    }

    // Method to import options from a CSV file
    private void importFromCSV(MultipartFile file) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {

            List<String[]> rows = csvReader.readAll();
            List<Option> options = new ArrayList<>();

            // Assuming first row is header, so start from index 1
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Option option = new Option();
                option.setName(row[0]); // Assuming first column contains the name of the option
                options.add(option);
            }

            // Save the options to the database
            optionRepository.saveAll(options);
        }
    }

    // Method to import options from an XLSX file
    private void importFromXLSX(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            List<Option> options = new ArrayList<>();

            // Skip header row
            if (rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Option option = new Option();

                // Assuming first cell contains the name of the option
                Cell cell = row.getCell(0);
                if (cell != null) {
                    option.setName(cell.getStringCellValue());
                    options.add(option);
                }
            }

            // Save the options to the database
            optionRepository.saveAll(options);
        }
    }
}