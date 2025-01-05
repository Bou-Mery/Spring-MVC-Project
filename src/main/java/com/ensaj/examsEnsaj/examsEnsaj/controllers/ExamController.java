package com.ensaj.examsEnsaj.examsEnsaj.controllers;

import com.ensaj.examsEnsaj.examsEnsaj.entites.*;
import com.ensaj.examsEnsaj.examsEnsaj.respository.DepartementRepository;
import com.ensaj.examsEnsaj.examsEnsaj.respository.OptionRepository;
import com.ensaj.examsEnsaj.examsEnsaj.services.EnseignantService;
import com.ensaj.examsEnsaj.examsEnsaj.services.ExamService;
import com.ensaj.examsEnsaj.examsEnsaj.services.LocalService;
import com.ensaj.examsEnsaj.examsEnsaj.services.SessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ExamController {
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    private ExamService examService;
    @Autowired
    DepartementRepository departementRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private LocalService localService;
    @Autowired
    private EnseignantService enseignantService;
    @Autowired
    private SurvellenceService survellenceService;

    @GetMapping("/enseignants-by-department")
    @ResponseBody
    public List<Ensiegnent> getEnseignantsByDepartment(@RequestParam String departmentName) {
        Departement departement = departementRepository.findByNomDepartement(departmentName);
        if (departement != null) {
            return enseignantService.getEnseignantsByDepartement(departement);
        }
        return new ArrayList<>();
    }
    @GetMapping("/locaux")
    @ResponseBody
    public List<Local> getLocaux() {
        return localService.getAllLocaux();
    }


    @GetMapping("/exam/{sessionId}")
    public String examensPage(@PathVariable int sessionId, Model model ,HttpSession httpSession) {
        // Récupérer la session
        Session session = sessionService.getSessionById(sessionId);
        List<Local> locaux = localService.getAllLocaux();
        List<Option> options=optionRepository.findAll();
        List<Departement> departements=departementRepository.findAll();
        model.addAttribute("locaux", locaux);
        model.addAttribute("csession", session);
        model.addAttribute("options", options);
        model.addAttribute("departements", departements);
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession);

        if (session == null) {
            model.addAttribute("errorMessage", "Session not found");
            return "error";
        }

        if (session.getDateDebut() == null || session.getDateFin() == null) {
            model.addAttribute("errorMessage", "Dates de la session non définies");
            return "error";
        }

        try {
            List<String> dates = generateDatesBetween(session.getDateDebut().toString(), session.getDateFin().toString());
            List<String> creneaux = generateCreneaux(session);

            if (dates == null || dates.isEmpty() || creneaux == null || creneaux.isEmpty()) {
                model.addAttribute("errorMessage", "Dates ou créneaux non disponibles");
                return "error";
            }

            model.addAttribute("dates", dates);
            model.addAttribute("creneaux", creneaux);
            System.out.println("Dates: " + dates);
            System.out.println("Créneaux: " + creneaux);
        } catch (ParseException e) {
            model.addAttribute("errorMessage", "Erreur lors du traitement des dates");
            return "error";
        }

        return "exams";
    }
    @PostMapping("/addExam")
    public String addExam(@RequestParam String dateExamen,
                          @RequestParam String heureExamen,
                          @RequestParam String module,
                          @RequestParam String option,
                          @RequestParam String responsableModule,
                          @RequestParam int nombreEtudiants,
                          @RequestParam String locauxExamenIds,  // Changer le type en String
                          @RequestParam(required = false) Integer sessionId,
                          Model model) {

        // Validation de sessionId
        if (sessionId == null) {
            model.addAttribute("errorMessage", "Session ID is missing");
            return "error";
        }

        // Récupération de la session
        Session session = sessionService.getSessionById(sessionId);
        if (session == null) {
            model.addAttribute("errorMessage", "Session not found for ID: " + sessionId);
            return "error";
        }

        // Transformation et validation des IDs des locaux
        List<Integer> ids;
        try {
            ids = Arrays.stream(locauxExamenIds.replaceAll("[\\[\\]]", "").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid locauxExamenIds format");
            return "error";
        }

        // Récupération des locaux
        List<Local> locaux = localService.getLocauxByIds(ids);
        if (locaux == null || locaux.isEmpty()) {
            model.addAttribute("errorMessage", "No valid locaux found for provided IDs");
            return "error";
        }

        // Création de l'objet Exam
        Exam exam = new Exam();
        exam.setDateExamen(dateExamen);
        exam.setHeureExamen(heureExamen);
        exam.setModule(module);
        exam.setOpt(option);
        exam.setResponsableModule(responsableModule);
        exam.setNombreEtudiants(nombreEtudiants);
        System.out.println("locaux: " + locaux);
        exam.setLocaux(locaux);
        List<Ensiegnent> ensiegnents=enseignantService.getAllEnseignants();
        List<Survellence> survellences=new ArrayList<>();
        for (Local l : locaux) {
            Survellence survellence=new Survellence();
            survellence.setHeureExamen(heureExamen);
            survellence.setDateExamen(dateExamen);
            survellence.setLocal(l);
            survellences.add(survellence);
        }
        for(int i=0;i<survellences.size();i++) {
            if((!responsableModule.contains(ensiegnents.get(i).getNom()))) {
                survellences.get(i).setEnseignant(ensiegnents.get(i).getNom());
            }
            else{
                survellences.get(i).setEnseignant(ensiegnents.get(i).getNom());
                survellences.get(i).getLocal().setNom("TTL");
            }

        }
        for (Survellence s : survellences) {
            survellenceService.save(s);
        }

        exam.setSession(session);

        // Sauvegarde dans la base de données
        try {
            examService.creerExam(exam);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to save exam: " + e.getMessage());
            return "error";
        }
        return "redirect:/exam/" + sessionId;
    }


    @PostMapping("/deleteExam")
    public String deleteExam(@RequestParam("id") int id,
                             HttpSession httpSession,
                             RedirectAttributes redirectAttributes) {

        Session currentSession = null;
        try {

            currentSession = (Session) httpSession.getAttribute("currentSession");

            if (currentSession == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Session invalide");
                return "redirect:/exams";
            }

            // Supprimer l'examen
            examService.deleteExam(id);

            // Ajouter un message de succès
            redirectAttributes.addFlashAttribute("successMessage", "L'examen a été supprimé avec succès");

            // Rediriger vers la page de la session
            return "redirect:/exam/" + currentSession.getIdSession();

        } catch (Exception e) {
            // Ajouter un message d'erreur
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer l'examen : " + e.getMessage());

            // Redirection en cas d'erreur
            return "redirect:/exam/" + currentSession.getIdSession();
        }
    }

    public List<String> generateDatesBetween(String dateDebut, String dateFin) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date debut = sdf.parse(dateDebut);
        Date fin = sdf.parse(dateFin);

        List<String> dates = new ArrayList<>();

        dates.add(sdf.format(debut));

        while (debut.before(fin)) {
            debut = new Date(debut.getTime() + (1000 * 60 * 60 * 24));
            dates.add(sdf.format(debut));
        }

        if (!debut.equals(fin)) {
            dates.add(sdf.format(fin));
        }

        return dates;
    }
    private List<String> generateCreneaux(Session session) {
        List<String> creneaux = new ArrayList<>();

        if (session.getHeureMatinDebut() != null && session.getHeureMatinFin() != null) {
            creneaux.addAll(createCreneaux(session.getHeureMatinDebut(), session.getHeureMatinFin()));
        }

        if (session.getHeureSoirDebut() != null && session.getHeureSoirFin() != null) {
            creneaux.addAll(createCreneaux(session.getHeureSoirDebut(), session.getHeureSoirFin()));
        }

        return creneaux;
    }

    private List<String> createCreneaux(String heureDebut, String heureFin) {
        List<String> creneaux = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            Date debut = sdf.parse(heureDebut);
            Date fin = sdf.parse(heureFin);

            long duration = 2 * 60 * 60 * 1000;

            Date currentStart = debut;

            while (currentStart.before(fin)) {

                Date currentEnd = new Date(currentStart.getTime() + duration);

                if (currentEnd.after(fin)) {
                    currentEnd = fin;
                }

                creneaux.add(sdf.format(currentStart) + " - " + sdf.format(currentEnd));

                currentStart = new Date(currentEnd.getTime() + 5 * 60 * 1000);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return creneaux;
    }
    @GetMapping("rec_exam/{date}/{heure}")
    public String recExam(@PathVariable String  date, @PathVariable String  heure, HttpSession httpSession,Model model ) {
        List<Exam> exams =examService.getExamByDateAndTime(date,heure);
        model.addAttribute("exams", exams);
        Session currentSession = (Session) httpSession.getAttribute("currentSession");
        model.addAttribute("currentSession", currentSession); // Ajoutez cette ligne
        return "all_exam";

    }
}
