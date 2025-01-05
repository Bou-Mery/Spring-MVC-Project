package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.services.AdminService;
import com.ensaj.examsEnsaj.examsEnsaj.services.SessionService;
import com.ensaj.examsEnsaj.examsEnsaj.services.mailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Admin;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("admin")
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionService sessionService;



    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(Admin admin, Model model, HttpSession session) {
        Admin existingAdmin = adminService.findByUsername(admin.getUsername());

        if (existingAdmin != null && existingAdmin.getPassword().equals(admin.getPassword()) ) {
            session.setAttribute("admin", existingAdmin);
            model.addAttribute("admin", existingAdmin);

            List<Session> sessions = sessionService.getAllSessions();
            model.addAttribute("sessions", sessions);

            return "home";
        }

        model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect !");
        return "login";
    }

    @PostMapping("/mailService")
    public String recoverPassword(String email, Model model) {
        Admin existingAdmin = adminService.findByEmail(email);

        if (existingAdmin != null) {
            mailService servicemail = new mailService();
            String resetToken = "1234"; // Générer un token sécurisé ici
            servicemail.sendEmail(existingAdmin.getEmail(), "Récupération de mot de passe", "Votre code : " + resetToken);
            return "redirect:/recupModepasse";
        }

        model.addAttribute("error", "Adresse e-mail introuvable !");
        return "login";
    }
}
