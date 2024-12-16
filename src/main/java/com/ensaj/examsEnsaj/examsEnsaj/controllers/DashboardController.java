package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.*;
import com.ensaj.examsEnsaj.examsEnsaj.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class DashboardController {

    private final SessionService sessionService;
    private final DepartementService departementService;
    private final ExamService examService;
    private final EnseignantService enseignantService;

    public DashboardController(SessionService sessionService, DepartementService departementService, ExamService examService, EnseignantService enseignantService) {
        this.sessionService = sessionService;
        this.departementService = departementService;
        this.examService = examService;
        this.enseignantService = enseignantService;
    }
    @GetMapping("/dashboard")
    public String showDashboard(@RequestParam int id, Model model, HttpSession httpSession) {
        // Récupérer la session par ID
        Session session = sessionService.getSessionById(id);
        if (session == null) {
            model.addAttribute("error", "Session introuvable !");
            return "error"; // Rediriger vers une page d'erreur si la session n'est pas trouvée
        }
        model.addAttribute("session", session);

        // Récupérer la session courante
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession);

        // Récupérer les statistiques
        List<Departement> allDepartements = departementService.getAllDepartements();
        model.addAttribute("totalDepartements", allDepartements.size());

        List<Exam> allExams = examService.getAllExams();
        model.addAttribute("totalExams", allExams.size());

        List<Ensiegnent> allEnseignants = enseignantService.getAllEnseignants();
        model.addAttribute("totalEnseignants", allEnseignants.size());

        // Récupérer les examens récents
        List<Exam> recentExams = examService.getRecentExams();
        model.addAttribute("recentExams", recentExams);

        // Préparer les données pour les graphiques
        if (recentExams.isEmpty()) {
            model.addAttribute("examLabels", new String[0]);
            model.addAttribute("examCounts", new Integer[0]);
        } else {
            model.addAttribute("examLabels", recentExams.stream().map(Exam::getModule).toArray(String[]::new));
            model.addAttribute("examCounts", recentExams.stream().map(Exam::getNombreEtudiants).toArray(Integer[]::new));
        }

        // Récupérer les enseignants par département
        List<Departement> departements = departementService.getAllDepartements();
        model.addAttribute("teachersByDepartmentLabels", departements.stream().map(Departement::getNomDepartement).toArray(String[]::new));
        model.addAttribute("teachersByDepartmentCounts", departements.stream().map(departement -> enseignantService.getEnseignantsByDepartement(departement).size()).toArray(Integer[]::new));

        // Debugging: Afficher les données dans la console
        System.out.println("Exam Labels: " + Arrays.toString((String[]) model.getAttribute("examLabels")));
        System.out.println("Exam Counts: " + Arrays.toString((Integer[]) model.getAttribute("examCounts")));
        System.out.println("Teachers by Department Labels: " + Arrays.toString((String[]) model.getAttribute("teachersByDepartmentLabels")));
        System.out.println("Teachers by Department Counts: " + Arrays.toString((Integer[]) model.getAttribute("teachersByDepartmentCounts")));

        return "layouts/dashboard"; // Vue correspondante
    }
}