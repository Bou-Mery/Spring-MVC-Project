package com.ensaj.examsEnsaj.examsEnsaj.respository;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Ensiegnent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnseignantRepository extends JpaRepository<Ensiegnent, Integer> {
    List<Ensiegnent> findByDepartement(Departement departement);
    boolean existsByNomAndPrenomAndEmail(String nom, String prenom, String email);
}