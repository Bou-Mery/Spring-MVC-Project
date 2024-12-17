package com.ensaj.examsEnsaj.examsEnsaj.services;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Module;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Option;
import com.ensaj.examsEnsaj.examsEnsaj.respository.ModuleRepository;
import com.ensaj.examsEnsaj.examsEnsaj.respository.OptionRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ModuleService {
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    // Ajouter un module
    public Module addModule(Module module) {
        return moduleRepository.save(module);
    }

    // Récupérer tous les modules
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    // Récupérer un module par son ID
    public Module getModuleById(int id) {
        return moduleRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Module introuvable avec ID : " + id));
    }

    // Mettre à jour un module
    public Module updateModule(int id, Module moduleDetails) {
        Module module = moduleRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Module introuvable avec ID : " + id));

        module.setNom(moduleDetails.getNom());
        module.setOption(moduleDetails.getOption());

        return moduleRepository.save(module);
    }

    // Supprimer un module
    public void deleteModule(int id) {
        Module module = moduleRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Module introuvable avec ID : " + id));

        moduleRepository.delete(module);
    }
    public void importModules(MultipartFile file, int optionId) throws Exception {
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".csv")) {
            // Traitement CSV
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(","); // Suppose que les colonnes sont séparées par des virgules
                    String moduleName = data[0];
                    saveModule(moduleName, optionId);
                }
            }
        } else if (fileName.endsWith(".xlsx")) {
            // Traitement Excel
            try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    Cell cell = row.getCell(0); // Suppose que le nom du module est dans la 1ère colonne
                    if (cell != null) {
                        String moduleName = cell.getStringCellValue();
                        saveModule(moduleName, optionId);
                    }
                }
            }
        } else {
            throw new Exception("Format de fichier non supporté. Veuillez importer un fichier CSV ou Excel.");
        }
    }

    private void saveModule(String moduleName, int optionId) {
        // Vérification de l'option
        Option option = optionRepository.findById(optionId).orElseThrow();

        Module module = new Module();
        module.setNom(moduleName);
        module.setOption(option);  // Assurez-vous que Module a une relation avec Option

        moduleRepository.save(module);
    }


}
