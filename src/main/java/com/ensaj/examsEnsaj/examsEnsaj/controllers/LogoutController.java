package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Invalider la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Ajouter un message de succès ou d'information
        redirectAttributes.addFlashAttribute("successMessage", "Vous avez été déconnecté avec succès.");

        // Rediriger vers la page de connexion ou une autre page
        return "redirect:/login"; // Remplacez "/login" par l'URL de votre page de connexion
    }
}