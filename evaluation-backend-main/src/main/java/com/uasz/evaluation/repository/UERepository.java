package com.uasz.evaluation.repository;

import com.uasz.evaluation.model.UE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UERepository extends JpaRepository<UE, Integer> {
    Optional<UE> findByCodeUE(String codeUE);
}