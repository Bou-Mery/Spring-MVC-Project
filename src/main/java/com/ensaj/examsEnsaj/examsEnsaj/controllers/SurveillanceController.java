package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SurveillanceController {

    @GetMapping("/surveillance")
    public String getServeillance(Model model, HttpSession httpSession) {
        // Récupérer la session courante
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession);

        return "surveillance"; // This should match the name of your Thymeleaf template
    }
}