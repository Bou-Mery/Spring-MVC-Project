package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Survellence;
import com.ensaj.examsEnsaj.examsEnsaj.entites.SurvellenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SurveillanceController {

    private final SurvellenceService survellenceService;

    @Autowired
    public SurveillanceController(SurvellenceService survellenceService) {
        this.survellenceService = survellenceService;
    }

    @GetMapping("/surveillance")
    public String listSurvellence(Model model) {
        List<Survellence> survellences = survellenceService.findAll();
        model.addAttribute("survellences", survellences);
        return "surveillance"; // Chemin du template Thymeleaf
    }
}
