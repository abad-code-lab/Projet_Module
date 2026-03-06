package com.uasz.evaluation.controller;

import com.uasz.evaluation.dto.ConnexionDTO;
import com.uasz.evaluation.dto.JwtResponseDTO;
import com.uasz.evaluation.dto.UtilisateurCreationDTO;
import com.uasz.evaluation.model.Utilisateur;
import com.uasz.evaluation.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping({"/inscription", "/auth/register"})
    public ResponseEntity<Utilisateur> inscription(@Valid @RequestBody UtilisateurCreationDTO utilisateurCreationDTO) {
        return new ResponseEntity<>(utilisateurService.inscription(utilisateurCreationDTO), HttpStatus.CREATED);
    }

    @PostMapping({"/connexion", "/auth/signin"})
    public ResponseEntity<JwtResponseDTO> connexion(@Valid @RequestBody ConnexionDTO connexionDTO) {
        return ResponseEntity.ok(utilisateurService.connexion(connexionDTO));
    }

    @GetMapping({"/whoami", "/auth/whoami"})
    public ResponseEntity<?> whoami(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Utilisateur user = utilisateurService.findByEmail(principal.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        return ResponseEntity.ok(response);
    }
}