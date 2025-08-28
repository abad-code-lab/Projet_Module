package com.uasz.evaluation.repository;

import com.uasz.evaluation.model.EC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ECRepository extends JpaRepository<EC, Integer> {
    Optional<EC> findByCodeEC(String codeEC);
}