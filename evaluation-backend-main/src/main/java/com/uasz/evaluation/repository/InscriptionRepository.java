package com.uasz.evaluation.repository;

import com.uasz.evaluation.model.EC;
import com.uasz.evaluation.model.Etudiant;
import com.uasz.evaluation.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Integer> {
    List<Inscription> findByAnneeInscription(Integer annee);
    List<Inscription> findByEtudiant(Etudiant etudiant);
    List<Inscription> findByEc(EC ec);
    Optional<Inscription> findByAnneeInscriptionAndEtudiantAndEc(Integer annee, Etudiant etudiant, EC ec);
	Optional<Inscription> findByEtudiantAndEcAndAnneeInscription(Etudiant etudiant, EC ec, int annee);
}