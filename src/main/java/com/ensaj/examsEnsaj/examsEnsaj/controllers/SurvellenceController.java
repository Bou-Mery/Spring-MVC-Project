package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.*;

import com.ensaj.examsEnsaj.examsEnsaj.services.DepartementService;
import com.ensaj.examsEnsaj.examsEnsaj.services.EnseignantService;
import com.ensaj.examsEnsaj.examsEnsaj.services.LocalService;
import com.ensaj.examsEnsaj.examsEnsaj.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/survellence")
public class SurvellenceController {

    private final SurvellenceService survellenceService;
    @Autowired
    DepartementService departementService;
    @Autowired
    LocalService localService;
    @Autowired
    EnseignantService enseignantService;
    @Autowired
    SessionService sessionService;


    @Autowired
    public SurvellenceController(SurvellenceService survellenceService) {
        this.survellenceService = survellenceService;
    }

    @GetMapping("/{id}")
    public String listSurvellence(@PathVariable int id, Model model) {
        // Récupération de la session
        Session session = sessionService.getSessionById(id);
        // Récupération des surveillances
        List<Survellence> surveillances = survellenceService.findAll();
        // Calcul des dates entre dateDebut et dateFin

        List<String> dates = getDatesBetween(session.getDateDebut(), session.getDateFin());

        // Ajout des données au modèle

        model.addAttribute("sessions", session);
        model.addAttribute("dates", dates);
        model.addAttribute("surveillances", surveillances);

        return "list";
    }
    // Afficher le formulaire pour ajouter une nouvelle surveillance
    @GetMapping("/nouveau")
    public String showAddForm(Model model) {
        model.addAttribute("survellence", new Survellence());
        return "survellence/form"; // Chemin du template Thymeleaf
    }

    // Enregistrer une nouvelle surveillance
    @PostMapping("/enregistrer")
    public String saveSurvellence(@ModelAttribute("survellence") Survellence survellence) {
        survellenceService.save(survellence);
        return "redirect:/survellence";
    }

    // Afficher le formulaire pour modifier une surveillance existante
    @GetMapping("/modifier/{id}")
    public String showUpdateForm(@PathVariable("id") int id, Model model) {
        Survellence survellence = survellenceService.findById(id);
        model.addAttribute("survellence", survellence);
        return "survellence/form"; // Chemin du template Thymeleaf
    }

    // Supprimer une surveillance
    @GetMapping("/supprimer/{id}")
    public String deleteSurvellence(@PathVariable("id") int id) {
        survellenceService.deleteById(id);
        return "redirect:/survellence";
    }
    private List<String> getDatesBetween(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<String> dates = new ArrayList<>();
        while (!start.isAfter(end)) {
            dates.add(start.format(formatter));
            start = start.plusDays(1);
        }
        return dates;
    }
}
