package com.ensaj.examsEnsaj.examsEnsaj.respository;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
}
