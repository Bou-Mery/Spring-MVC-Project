package com.ensaj.examsEnsaj.examsEnsaj.services;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Exam;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import com.ensaj.examsEnsaj.examsEnsaj.respository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void deleteExam(int id) {
        examRepository.deleteById(id);
    }
    public List<Exam> getExamByDateAndTime(String date,String heure) {
        return examRepository.findExamsByDateExamenAndAndHeureExamen(date,heure);
    }




}