package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.services.DepartementService;
import com.ensaj.examsEnsaj.examsEnsaj.services.EnseignantService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/departements")
public class DepartementController {

    @Autowired
    private DepartementService departementService;
    @Autowired
    private EnseignantService enseignantService;

    @GetMapping
    public String getAllDepartements(Model model , HttpSession httpSession) {

        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne

        List<Departement> departements = departementService.getAllDepartements();
        model.addAttribute("departements", departements); // Tous les départements
        return "layouts/departements"; // Vue HTML pour afficher les départements
    }

    @PostMapping("/add")
    public String addDepartement(@ModelAttribute Departement departement) {
        departementService.createDepartement(departement);
        return "redirect:/departements";
    }

    @GetMapping("/delete/{id}")
    public String deleteDepartement(@PathVariable int id) {
        departementService.deleteDepartement(id);
        return "redirect:/departements";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Departement departement = departementService.getDepartementById(id);
        model.addAttribute("departement", departement);
        return "departements/edit";
    }

    @PostMapping("/update")
    public String updateDepartement(@ModelAttribute Departement departement) {
        departementService.updateDepartement(departement.getIdDepartement(), departement);
        return "redirect:/departements";
    }
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Departement>> searchDepartements(@RequestParam("searchName") String searchName) {
        List<Departement> departements;

        try {
            if (searchName != null && !searchName.isEmpty()) {
                departements = departementService.searchDepartementsByName(searchName);
            } else {
                departements = departementService.getAllDepartements();
            }

            // Ensure the list is not null
            if (departements == null) {
                departements = new ArrayList<>();
            }

            return ResponseEntity.ok(departements);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error while searching departments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("message", "Fichier vide. Veuillez télécharger un fichier valide.");
            return ResponseEntity.badRequest().body(response);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // Assuming CSV is comma-separated
                Departement departement = new Departement();
                departement.setNomDepartement(data[0]); // Adjust index based on your CSV structure
                departementService.createDepartement(departement);
            }
            response.put("message", "Fichier importé avec succès.");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "Erreur lors de l'importation du fichier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/{id}/enseignants")
    public String getDepartementEnseignants(@PathVariable int id, Model model ,HttpSession httpSession) {
        Departement departement = departementService.getDepartementById(id);
        model.addAttribute("departement", departementService.getDepartementById(id));
        if (departement == null) {
            return "redirect:/departements";
        }
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne
        model.addAttribute("departement", departement);
        model.addAttribute("enseignants", departement.getEnseignants());
        return "layouts/enseignants"; // Vue HTML pour afficher les enseignants du département
    }

}