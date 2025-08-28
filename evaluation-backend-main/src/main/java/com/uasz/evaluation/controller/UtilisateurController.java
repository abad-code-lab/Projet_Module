package com.uasz.evaluation.controller;

import com.uasz.evaluation.dto.ConnexionDTO;
import com.uasz.evaluation.dto.JwtResponseDTO;
import com.uasz.evaluation.dto.UtilisateurCreationDTO;
import com.uasz.evaluation.model.Utilisateur;
import com.uasz.evaluation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/inscription")
    public ResponseEntity<Utilisateur> inscription(@Valid @RequestBody UtilisateurCreationDTO utilisateurCreationDTO) {
        return new ResponseEntity<>(utilisateurService.inscription(utilisateurCreationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/connexion")
    public ResponseEntity<JwtResponseDTO> connexion(@Valid @RequestBody ConnexionDTO connexionDTO) {
        return ResponseEntity.ok(utilisateurService.connexion(connexionDTO));
    }
}