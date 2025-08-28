package com.uasz.evaluation.controller;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uasz.evaluation.service.ImportExportService;

@RestController
public class ImportExportController {
    
    @Autowired
    private ImportExportService importExportService;
    
    @PostMapping("/import/etudiants")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> importEtudiantsPourEC(
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam(value = "annee", required = false) Integer annee,
            @RequestParam(value = "codeEC", required = false) String codeEC,
            @RequestParam(value = "codeUE", required = false) String codeUE) {
        
        try {
            // Extraction des paramètres depuis le nom de fichier
            String filename = fichier.getOriginalFilename();
            String[] parts = filename.split("_");
            
            int actualYear = (annee != null) ? annee : Integer.parseInt(parts[0]);
            String actualCodeUE = (codeUE != null) ? codeUE : parts[1];
            String actualCodeEC = (codeEC != null) ? codeEC : parts[2];
            
            Map<String, Integer> stats = importExportService.importEtudiantsPourEC(
                fichier, actualCodeEC, actualCodeUE, actualYear);
            
            return ResponseEntity.ok(Map.of(
                "message", "Import réussi",
                "stats", stats
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Erreur lors de l'import: " + e.getMessage()
            );
        }
    }

//    public ResponseEntity<Map<String, Object>> importEtudiants(@RequestParam("fichier") MultipartFile fichier) {
//        try {
//            int count = importExportService.importEtudiants(fichier);
//            
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Import réussi");
//            response.put("etudiants_importes", count);
//            
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (IOException | CsvValidationException e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "Erreur lors de l'import: " + e.getMessage());
//            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
    @GetMapping("/export/evaluations")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSEUR')")
    public ResponseEntity<String> exportEvaluations() {
        try {
            String csvContent = importExportService.exportEvaluations();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "evaluations.csv");
            
            return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur lors de l'export: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}