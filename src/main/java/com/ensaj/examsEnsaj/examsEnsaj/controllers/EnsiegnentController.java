package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Ensiegnent;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.services.DepartementService;
import com.ensaj.examsEnsaj.examsEnsaj.services.EnseignantService;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EnsiegnentController {

    @Autowired
    private EnseignantService enseignantService;

    @Autowired
    private DepartementService departementService;

    @PostMapping("/enseignants/save")
    public String saveEnseignant(@ModelAttribute Ensiegnent enseignant,
                                 @RequestParam(required = false) Integer departementId,
                                 RedirectAttributes redirectAttributes
                                 ,Model model
                                , HttpSession httpSession) {
        if (departementId != null) {
            Departement departement = departementService.getDepartementById(departementId);

            enseignant.setDepartement(departement);
        }
        if (enseignantService.existsByNomAndPrenomAndEmail(enseignant.getNom(), enseignant.getPrenom(), enseignant.getEmail())) {
            return "redirect:/departements/" + departementId + "/enseignants";
        }
        enseignantService.createEnseignant(enseignant);
        redirectAttributes.addFlashAttribute("successMessage", "Enseignant ajouté avec succès");

        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne

        // Rediriger vers la page du département si possible
        return departementId != null
                ? "redirect:/departements/" + departementId + "/enseignants"
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


//    @GetMapping("/enseignants/supprimer/{id}")
//    public String supprimerEnseignant(@PathVariable int id,
//                                      @RequestParam(required = true) Integer departementId,
//                                      RedirectAttributes redirectAttributes) {
//        try {
//            // Verify if the department exists before proceeding with deletion
//            Departement departement = departementService.getDepartementById(departementId);
//            if (departement == null) {
//                redirectAttributes.addFlashAttribute("errorMessage", "Département non trouvé");
//                return "redirect:/departements";
//            }
//
//            // Supprimer l'enseignant
//            enseignantService.deleteEnseignant(id);
//
//            // Ajouter un message de succès
//            redirectAttributes.addFlashAttribute("successMessage", "Enseignant supprimé avec succès");
//        } catch (Exception e) {
//            // Ajouter un message d'erreur en cas de problème
//            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression de l'enseignant");
//        }
//
//        // Rediriger vers la liste des enseignants du département
//        return "redirect:/departements/" + departementId + "/enseignants";
//    }


    @GetMapping("/enseignants/supprimer/{id}")
    public String supprimerEnseignant(@PathVariable int id,
                                      @RequestParam(required = true) Integer departementId,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Vérifier si le département existe avant de procéder à la suppression
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



    @PostMapping("/enseignants/import/{id}")
    public String importEnseignants(@RequestParam("file") MultipartFile file, Model model, @PathVariable int id) {
        try {
            String fileType = file.getContentType();
            List<Ensiegnent> nouveauxEnseignants = new ArrayList<>();
            InputStream inputStream = file.getInputStream();

            if ("text/csv".equals(fileType)) {
                // Parser CSV
                CSVParser parser = CSVFormat.DEFAULT.withHeader("Nom", "Prénom", "Email", "Numéro", "Disponibilité")
                        .parse(new InputStreamReader(inputStream));

                for (CSVRecord record : parser) {
                    String nom = record.get("Nom");
                    String prenom = record.get("Prénom");
                    String email = record.get("Email");
                    String numero = record.get("Numéro");
                    String disponibilite =record.get("Disponibilité");

                    // Vérification de l'existence
                    if (!enseignantService.existsByNomAndPrenomAndEmail(nom, prenom, email)) {
                        Ensiegnent enseignant = new Ensiegnent();

                        enseignant.setNom(nom);
                        enseignant.setPrenom(prenom);
                        enseignant.setEmail(email);
                        enseignant.setNumero(numero);
                        enseignant.setDisponibilite(disponibilite);
                        enseignant.setDepartement(departementService.getDepartementById(id));
                        nouveauxEnseignants.add(enseignant);
                    }
                }

            } else if ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(fileType)) {
                // Parser Excel
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Ignorer la ligne d'en-tête

                    String nom = row.getCell(0).getStringCellValue();
                    String prenom = row.getCell(1).getStringCellValue();
                    String email = row.getCell(2).getStringCellValue();
                    String numero = row.getCell(3).getStringCellValue();
                    String disponibilite =row.getCell(4).getStringCellValue();

                    // Vérification de l'existence
                    if (!enseignantService.existsByNomAndPrenomAndEmail(nom, prenom, email)) {
                        Ensiegnent enseignant = new Ensiegnent();
                        enseignant.setNom(nom);
                        enseignant.setPrenom(prenom);
                        enseignant.setEmail(email);
                        enseignant.setNumero(numero);
                        enseignant.setDisponibilite(disponibilite);
                        enseignant.setDepartement(departementService.getDepartementById(id));
                        nouveauxEnseignants.add(enseignant);
                    }
                }
            } else {
                model.addAttribute("error", "Format de fichier non supporté !");
                return "redirect:/enseignants?error=format";
            }

            // Sauvegarder les nouveaux enseignants
            if (!nouveauxEnseignants.isEmpty()) {
                enseignantService.saveAll(nouveauxEnseignants);
            }

            model.addAttribute("success", "Fichier importé avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'importation du fichier !");
        }

        return "redirect:/departements/"+id+"/enseignants";
    }


}