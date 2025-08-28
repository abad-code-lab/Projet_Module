package com.uasz.evaluation.repository;

import com.uasz.evaluation.model.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Integer> {
    Optional<Etudiant> findByMatricule(String matricule);
//    Optional<Etudiant> findByEmail(String email);
}