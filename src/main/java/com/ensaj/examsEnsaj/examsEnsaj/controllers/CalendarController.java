package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String showCalendar(Model model, HttpSession httpSession) {
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne
        return "layouts/calendar"; // This should match the name of your HTML file without the extension
    }
}