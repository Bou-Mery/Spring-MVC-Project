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

import java.util.List;


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
    public List<Departement> searchDepartements(@RequestParam("searchName") String searchName) {
        if (searchName != null && !searchName.isEmpty()) {
            return departementService.searchDepartementsByName(searchName);
        }
        return departementService.getAllDepartements();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {

            String contentType = file.getContentType();
            if (!"text/csv".equals(contentType) && !"application/vnd.ms-excel".equals(contentType)
                    && !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                return ResponseEntity.badRequest().body("Format de fichier non supporté.");
            }


            List<Departement> departements = departementService.parseFile(file);
            departementService.saveAll(departements);

            return ResponseEntity.ok("Fichier importé avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation.");
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