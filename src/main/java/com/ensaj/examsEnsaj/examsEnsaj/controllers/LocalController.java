package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Local;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.services.LocalService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;


import javax.swing.text.html.CSS;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LocalController {

    @Autowired
    private LocalService localService;

    @GetMapping("/locals")
    public String getAllLocals(Model model, HttpSession httpSession) {
        List<Local> locals = localService.getAllLocals();

        model.addAttribute("locals", locals);
        model.addAttribute("local", new Local());

        // Récupérer la session courante
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne

        return "layouts/local";
    }
    @PostMapping("/locals/save")
    public String saveLocal(@ModelAttribute("local") Local local, Model model) {
        if (!localService.existsByNomAndTailleAndType(local.getNom(), local.getTaille(), local.getType())) {
            localService.createLocal(local);
            return "redirect:/locals?success=true";
        } else {
            model.addAttribute("error", "Le local existe déjà !");
            return "redirect:/locals?error=exists";
        }
    }

    @PostMapping("/locals/update/{id}")
    public String updateLocal(@PathVariable("id") int id, @ModelAttribute("local") Local updatedLocal) {
        Local existingLocal = localService.getLocalById(id);
        if (existingLocal != null) {
            existingLocal.setNom(updatedLocal.getNom());
            existingLocal.setTaille(updatedLocal.getTaille());
            existingLocal.setType(updatedLocal.getType());
            localService.updateLocal(existingLocal);
        }
        return "redirect:/locals?success=updated";
    }

    @GetMapping("/locals/delete/{id}")
    public String deleteLocal(@PathVariable("id") int id) {
        localService.deleteLocal(id);
        return "redirect:/locals?success=deleted";
    }

    @PostMapping("/locals/import")
    public String importLocaux(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String fileType = file.getContentType();
            List<Local> nouveauxLocaux = new ArrayList<>();
            InputStream inputStream = file.getInputStream();

            if ("text/csv".equals(fileType)) {
                CSVParser parser = CSVFormat.DEFAULT.withHeader("Nom", "Taille", "Type").parse(new InputStreamReader(inputStream));
                for (CSVRecord record : parser) {
                    String nom = record.get("Nom");
                    double taille = Double.parseDouble(record.get("Taille"));
                    String type = record.get("Type");

                    if (!localService.existsByNomAndTailleAndType(nom, taille, type)) {
                        Local local = new Local();
                        local.setNom(nom);
                        local.setTaille(taille);
                        local.setType(type);
                        nouveauxLocaux.add(local);
                    }
                }
            } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(fileType)) {
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Ignorer la ligne d'en-tête
                    String nom = row.getCell(0).getStringCellValue();
                    double taille = row.getCell(1).getNumericCellValue();
                    String type = row.getCell(2).getStringCellValue();
                    if (!localService.existsByNomAndTailleAndType(nom, taille, type)) {
                        Local local = new Local();
                        local.setNom(nom);
                        local.setTaille(taille);
                        local.setType(type);
                        nouveauxLocaux.add(local);
                    }
                }
            } else {
                model.addAttribute("error", "Format de fichier non supporté !");
                return "redirect:/locals?error=format"; // Redirection en cas de format non supporté
            }

            if (!nouveauxLocaux.isEmpty()) {
                localService.saveAll(nouveauxLocaux); // Sauvegarde des nouveaux locaux
            }
            model.addAttribute("success", "Fichier importé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'importation du fichier !");
        }

        return "redirect:/locals";
    }
}