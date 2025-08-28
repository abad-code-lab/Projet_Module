package com.uasz.evaluation.repository;

import com.uasz.evaluation.model.Evaluation;
import com.uasz.evaluation.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    Optional<Evaluation> findByInscription(Inscription inscription);
 // Ajouter cette méthode
//    default boolean existsByInscription(Inscription inscription) {
//        return findByInscription(inscription).isPresent();
//    }
}