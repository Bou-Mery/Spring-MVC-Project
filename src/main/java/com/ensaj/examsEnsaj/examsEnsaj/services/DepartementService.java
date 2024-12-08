package com.ensaj.examsEnsaj.examsEnsaj.services;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Departement;
import com.ensaj.examsEnsaj.examsEnsaj.respository.DepartementRepository;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;


@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * Récupère tous les départements
     *
     * @return Liste de tous les départements
     */
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }



    /**
     * Crée un nouveau département
     *
     * @param departement Détails du nouveau département
     * @return Département créé
     */
    public Departement createDepartement(Departement departement) {
        if (departement.getNomDepartement() == null || departement.getNomDepartement().isEmpty()) {
            throw new IllegalArgumentException("Le nom du département ne peut pas être vide.");
        }
        return departementRepository.save(departement);
    }

    /**
     * Met à jour un département existant.
     *
     * @param id ID du département à mettre à jour.
     * @param departementDetails Nouvelles données du département.
     * @return Département mis à jour.
     * @throws IllegalArgumentException si le département n'existe pas.
     */
    public Departement updateDepartement(int id, Departement departementDetails) {

        Departement existingDepartement = departementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aucun département trouvé avec l'ID " + id));


        if (departementDetails.getNomDepartement() != null && !departementDetails.getNomDepartement().isEmpty()) {
            existingDepartement.setNomDepartement(departementDetails.getNomDepartement());
        }


        return departementRepository.save(existingDepartement);
    }


    /**
     * Supprime un département par son ID
     *
     * @param id ID du département
     * @return true si le département est supprimé, false s'il n'existe pas
     */
    public boolean deleteDepartement(int id) {
        if (departementRepository.existsById(id)) {
            departementRepository.deleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException("Aucun département trouvé avec l'ID " + id);
        }
    }
    /**
     * Recherche des départements par nom
     *
     * @param nomDepartement Le nom du département à rechercher
     * @return Liste des départements trouvés
     */
    public List<Departement> searchDepartementsByName(String nomDepartement) {
        return departementRepository.findByNomDepartementContainingIgnoreCase(nomDepartement);
    }


    public List<Departement> parseFile(MultipartFile file) throws Exception {
        List<Departement> departements = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Cell cell = row.getCell(0);
            if (cell != null) {
                Departement departement = new Departement();
                departement.setNomDepartement(cell.getStringCellValue());
                departements.add(departement);
            }
        }
        workbook.close();
        return departements;
    }

    public void saveAll(List<Departement> departements) {
        departementRepository.saveAll(departements);
    }
    public Departement getDepartementById(int id) {
        return departementRepository.findById(id).orElse(null);
    }
}