package com.ensaj.examsEnsaj.examsEnsaj.entites;

import com.ensaj.examsEnsaj.examsEnsaj.entites.Survellence;
import com.ensaj.examsEnsaj.examsEnsaj.respository.SurvellenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SurvellenceService {

    private final SurvellenceRepository survellenceRepository;

    @Autowired
    public SurvellenceService(SurvellenceRepository survellenceRepository) {
        this.survellenceRepository = survellenceRepository;
    }

    @Transactional(readOnly = true)
    public List<Survellence> findAll() {
        return survellenceRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Survellence findById(int id) {
        return survellenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Surveillance introuvable pour l'ID : " + id));
    }
    @Transactional
    public Survellence save(Survellence survellence) {
        return survellenceRepository.save(survellence);
    }
    @Transactional
    public void deleteById(int id) {
        survellenceRepository.deleteById(id);
    }
}
