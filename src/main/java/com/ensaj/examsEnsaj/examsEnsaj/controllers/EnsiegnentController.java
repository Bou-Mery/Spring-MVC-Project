package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Ensiegnent;
import com.ensaj.examsEnsaj.examsEnsaj.services.DepartementService;
import com.ensaj.examsEnsaj.examsEnsaj.services.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EnsiegnentController {

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private DepartementService departementService;

    @PostMapping("/enseignants/save")
    public String saveEnseignant(@ModelAttribute Ensiegnent enseignant,
                                 @RequestParam(required = false) Integer departementId,
                                 RedirectAttributes redirectAttributes) {
        if (departementId != null) {
            Departement departement = departementService.getDepartementById(departementId);

            enseignant.setDepartement(departement);
        }

        enseignantService.createEnseignant(enseignant);
        redirectAttributes.addFlashAttribute("successMessage", "Enseignant ajouté avec succès");

        // Rediriger vers la page du département si possible
        return departementId != null
                ? "redirect:departements/" + departementId + "/enseignants"
                : "redirect:/enseignants";
    }


    @PostMapping("/enseignants/modifier/{id}")
    public String modifierEnseignant(
            @PathVariable int id,
            @ModelAttribute Ensiegnent updatedEnseignant,
            @RequestParam(required = true) Integer departementId,
            RedirectAttributes redirectAttributes) {

        // Retrieve the department (departement) from the service
        Departement departement = departementService.getDepartementById(departementId);

        if (departement == null) {
            // If the department does not exist, add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Département non trouvé");
            return "redirect:/departements";
        }

        // Retrieve the existing teacher (enseignant) by ID
        Ensiegnent existingEnseignant = enseignantService.getEnsiegnentById(id);

        if (existingEnseignant != null) {
            // Update the teacher's fields with the new values from the form
            existingEnseignant.setNom(updatedEnseignant.getNom());
            existingEnseignant.setPrenom(updatedEnseignant.getPrenom());
            existingEnseignant.setNumero(updatedEnseignant.getNumero());
            existingEnseignant.setEmail(updatedEnseignant.getEmail());
            existingEnseignant.setDisponibilite(updatedEnseignant.getDisponibilite());
            existingEnseignant.setDepartement(departement);

            // Save the updated teacher (enseignant)
            enseignantService.updateEnseignant(id, existingEnseignant);

            // Add a success message to the redirect attributes
            redirectAttributes.addFlashAttribute("successMessage", "Enseignant modifié avec succès");
        } else {
            // If the teacher was not found, add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Enseignant non trouvé");
        }

        // Redirect to the list of teachers for the specific department
        return "redirect:/departements/" + departementId + "/enseignants";
    }


    @GetMapping("/enseignants/supprimer/{id}")
    public String supprimerEnseignant(@PathVariable int id,
                                      @RequestParam(required = true) Integer departementId,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Verify if the department exists before proceeding with deletion
            Departement departement = departementService.getDepartementById(departementId);
            if (departement == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Département non trouvé");
                return "redirect:/departements";
            }

            // Supprimer l'enseignant
            enseignantService.deleteEnseignant(id);

            // Ajouter un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "Enseignant supprimé avec succès");
        } catch (Exception e) {
            // Ajouter un message d'erreur en cas de problème
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression de l'enseignant");
        }

        // Rediriger vers la liste des enseignants du département
        return "redirect:/departements/" + departementId + "/enseignants";
    }
}