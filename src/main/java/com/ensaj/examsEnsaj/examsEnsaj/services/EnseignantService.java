package com.ensaj.examsEnsaj.examsEnsaj.services;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.entites.Ensiegnent;
import com.ensaj.examsEnsaj.examsEnsaj.respository.EnseignantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnseignantService {

    @Autowired
    private EnseignantRepository enseignantRepository;

    public List<Ensiegnent> getAllEnseignants() {
        return enseignantRepository.findAll();
    }

    public Ensiegnent getEnsiegnentById(int id) {
        Optional<Ensiegnent> optionalEnsiegnent = enseignantRepository.findById(id);
        return optionalEnsiegnent.orElse(null);
    }
    public Ensiegnent createEnseignant(Ensiegnent enseignant) {
        return enseignantRepository.save(enseignant);
    }

    // Mettre à jour un enseignant existant
    public Ensiegnent updateEnseignant(int id, Ensiegnent enseignantDetails) {
        return enseignantRepository.findById(id).map(existingEnseignant -> {
            existingEnseignant.setNom(enseignantDetails.getNom());
            existingEnseignant.setPrenom(enseignantDetails.getPrenom());
            existingEnseignant.setEmail(enseignantDetails.getEmail());
            existingEnseignant.setNumero(enseignantDetails.getNumero());
            existingEnseignant.setDisponibilite(enseignantDetails.getDisponibilite());
            existingEnseignant.setDepartement(enseignantDetails.getDepartement());
            return enseignantRepository.save(existingEnseignant);
        }).orElse(null);
    }
    public boolean deleteEnseignant(int id) {
        if (enseignantRepository.existsById(id)) {
            enseignantRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public List<Ensiegnent> getEnseignantsByDepartement(Departement departement) {
        return enseignantRepository.findByDepartement(departement);
    }
    public boolean existsByNomAndPrenomAndEmail(String nom, String prenom, String email) {
        return enseignantRepository.existsByNomAndPrenomAndEmail(nom, prenom, email);
    }
    public void saveAll(List<Ensiegnent> enseignants) {
        enseignantRepository.saveAll(enseignants);
    }

}