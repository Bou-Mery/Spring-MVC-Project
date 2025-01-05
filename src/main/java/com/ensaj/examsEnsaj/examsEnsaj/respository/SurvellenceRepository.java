package com.ensaj.examsEnsaj.examsEnsaj.respository;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Survellence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurvellenceRepository extends JpaRepository<Survellence, Integer> {
    // Vous pouvez ajouter des méthodes de recherche personnalisées ici, si nécessaire.
}
