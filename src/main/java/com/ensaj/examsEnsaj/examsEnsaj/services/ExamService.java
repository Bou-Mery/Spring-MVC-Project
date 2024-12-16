package com.ensaj.examsEnsaj.examsEnsaj.services;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Exam;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.respository.ExamRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;
    @Transactional
    public Exam creerExam(Exam exam) {
        return examRepository.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // Nouvelle méthode pour récupérer les examens par session
    public List<Exam> getExamsBySession(Session session) {
        return examRepository.findBySession(session);
    }

    // Récupérer un examen par son ID
    public Exam getExamById(int id) {
        return examRepository.findById(id).orElse(null);
    }

    // Supprimer un examen
    public List<Exam> getExamByDateAndTime(String date,String heure) {
        return examRepository.findExamsByDateExamenAndAndHeureExamen(date,heure);
    }
    public void deleteExam(int id) {
        // Optional: First check if exam exists
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with id: " + id));

        examRepository.deleteById(id);
    }

    public List<Exam> getRecentExams() {
        Pageable pageable = PageRequest.of(0, 5); // Récupérer les 5 derniers examens
        return examRepository.findTop5ByOrderByDateExamenDesc(pageable);
    }

}