package com.uasz.evaluation.service;

import com.uasz.evaluation.dto.ConnexionDTO;
import com.uasz.evaluation.dto.JwtResponseDTO;
import com.uasz.evaluation.dto.UtilisateurCreationDTO;
import com.uasz.evaluation.model.Utilisateur;
import com.uasz.evaluation.repository.UtilisateurRepository;
import com.uasz.evaluation.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    public Utilisateur inscription(UtilisateurCreationDTO utilisateurCreationDTO) {
        // Vérifier si l'email est déjà utilisé
        if (utilisateurRepository.existsByEmail(utilisateurCreationDTO.getEmail())) {
            throw new IllegalStateException("L'email est déjà utilisé");
        }
        
        // Créer un nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(utilisateurCreationDTO.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurCreationDTO.getMotDePasse()));
        utilisateur.setRole(utilisateurCreationDTO.getRole());
        
        return utilisateurRepository.save(utilisateur);
    }
    
    public JwtResponseDTO connexion(ConnexionDTO connexionDTO) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                connexionDTO.getEmail(),
                connexionDTO.getMotDePasse()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.generateToken(authentication);
        return new JwtResponseDTO(jwt);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}