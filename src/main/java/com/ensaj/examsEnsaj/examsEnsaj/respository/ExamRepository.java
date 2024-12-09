package com.ensaj.examsEnsaj.examsEnsaj.respository;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Exam;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    // Méthode pour trouver les examens par session
    List<Exam> findBySession(Session session);
    @Query("SELECT e FROM Exam e WHERE e.dateExamen = :date AND e.heureExamen = :time")
    List<Exam> getExamsByDateAndTime(@Param("date") String dateExamen, @Param("time") String heureExamen);

    List<Exam> findExamsByDateExamenAndAndHeureExamen(String date, String heure);
}