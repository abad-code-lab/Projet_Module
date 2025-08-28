package com.uasz.evaluation.service;

import com.uasz.evaluation.exception.ResourceNotFoundException;
import com.uasz.evaluation.model.EC;
import com.uasz.evaluation.model.Etudiant;
import com.uasz.evaluation.model.Inscription;
import com.uasz.evaluation.repository.ECRepository;
import com.uasz.evaluation.repository.EtudiantRepository;
import com.uasz.evaluation.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionService {
    
    @Autowired
    private InscriptionRepository inscriptionRepository;
    
    @Autowired
    private EtudiantRepository etudiantRepository;
    
    @Autowired
    private ECRepository ecRepository;
    
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }
    
    public Inscription getInscriptionById(Integer id) {
        return inscriptionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inscription", "id", id));
    }
    
    public Inscription createInscription(Inscription inscription) {
        // Vérifier si l'étudiant existe
        Etudiant etudiant = etudiantRepository.findById(inscription.getEtudiant().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", inscription.getEtudiant().getId()));
        
        // Vérifier si l'EC existe
        EC ec = ecRepository.findById(inscription.getEc().getId())
            .orElseThrow(() -> new ResourceNotFoundException("EC", "id", inscription.getEc().getId()));
        
        // Vérifier si l'inscription n'existe pas déjà
        inscriptionRepository.findByAnneeInscriptionAndEtudiantAndEc(
            inscription.getAnneeInscription(), etudiant, ec)
            .ifPresent(i -> {
                throw new IllegalStateException("L'étudiant est déjà inscrit à cet EC pour cette année");
            });
        
        inscription.setEtudiant(etudiant);
        inscription.setEc(ec);
        
        return inscriptionRepository.save(inscription);
    }
    
    public Inscription updateInscription(Integer id, Inscription inscriptionDetails) {
        Inscription inscription = getInscriptionById(id);
        
        // Vérifier si l'étudiant existe
        Etudiant etudiant = etudiantRepository.findById(inscriptionDetails.getEtudiant().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Etudiant", "id", inscriptionDetails.getEtudiant().getId()));
        
        // Vérifier si l'EC existe
        EC ec = ecRepository.findById(inscriptionDetails.getEc().getId())
            .orElseThrow(() -> new ResourceNotFoundException("EC", "id", inscriptionDetails.getEc().getId()));
        
        inscription.setAnneeInscription(inscriptionDetails.getAnneeInscription());
        inscription.setEtudiant(etudiant);
        inscription.setEc(ec);
        
        return inscriptionRepository.save(inscription);
    }
    
    public void deleteInscription(Integer id) {
        Inscription inscription = getInscriptionById(id);
        inscriptionRepository.delete(inscription);
    }
}