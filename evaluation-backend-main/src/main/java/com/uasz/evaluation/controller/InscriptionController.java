package com.uasz.evaluation.controller;

import com.uasz.evaluation.model.Inscription;
import com.uasz.evaluation.service.InscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/inscriptions")
public class InscriptionController {

    @Autowired
    private InscriptionService inscriptionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSEUR')")
    public ResponseEntity<List<Inscription>> getAllInscriptions() {
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSEUR', 'ETUDIANT')")
    public ResponseEntity<Inscription> getInscriptionById(@PathVariable Integer id) {
        return ResponseEntity.ok(inscriptionService.getInscriptionById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> createInscription(@Valid @RequestBody Inscription inscription) {
        return new ResponseEntity<>(inscriptionService.createInscription(inscription), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Inscription> updateInscription(
            @PathVariable Integer id,
            @Valid @RequestBody Inscription inscription) {
        return ResponseEntity.ok(inscriptionService.updateInscription(id, inscription));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInscription(@PathVariable Integer id) {
        inscriptionService.deleteInscription(id);
        return ResponseEntity.noContent().build();
    }
}