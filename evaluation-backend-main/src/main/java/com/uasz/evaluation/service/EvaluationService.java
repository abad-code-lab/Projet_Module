package com.uasz.evaluation.service;

import com.uasz.evaluation.exception.ResourceNotFoundException;
import com.uasz.evaluation.model.Evaluation;
import com.uasz.evaluation.model.Inscription;
import com.uasz.evaluation.repository.EvaluationRepository;
import com.uasz.evaluation.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationService {
    
    @Autowired
    private EvaluationRepository evaluationRepository;
    
    @Autowired
    private InscriptionRepository inscriptionRepository;
    
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }
    
    public Evaluation getEvaluationById(Integer id) {
        return evaluationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Evaluation", "id", id));
    }
    
    public Evaluation createEvaluation(Evaluation evaluation) {
        // Vérifier si l'inscription existe
        Inscription inscription = inscriptionRepository.findById(evaluation.getInscription().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Inscription", "id", evaluation.getInscription().getId()));
        
        // Vérifier si une évaluation existe déjà pour cette inscription
        evaluationRepository.findByInscription(inscription).ifPresent(e -> {
            throw new IllegalStateException("Une évaluation existe déjà pour cette inscription");
        });
        
        evaluation.setInscription(inscription);
        return evaluationRepository.save(evaluation);
    }
    
    public Evaluation updateEvaluation(Integer id, Evaluation evaluationDetails) {
        Evaluation evaluation = getEvaluationById(id);
        
        evaluation.setNoteControle(evaluationDetails.getNoteControle());
        evaluation.setNoteExamen(evaluationDetails.getNoteExamen());
        
        return evaluationRepository.save(evaluation);
    }
    
    public void deleteEvaluation(Integer id) {
        Evaluation evaluation = getEvaluationById(id);
        evaluationRepository.delete(evaluation);
    }
}