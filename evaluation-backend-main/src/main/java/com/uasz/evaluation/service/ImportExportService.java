package com.uasz.evaluation.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.uasz.evaluation.model.EC;
import com.uasz.evaluation.model.Etudiant;
import com.uasz.evaluation.model.Evaluation;
import com.uasz.evaluation.model.Inscription;
import com.uasz.evaluation.model.UE;
import com.uasz.evaluation.repository.ECRepository;
import com.uasz.evaluation.repository.EtudiantRepository;
import com.uasz.evaluation.repository.EvaluationRepository;
import com.uasz.evaluation.repository.InscriptionRepository;
import com.uasz.evaluation.repository.UERepository;

@Service
public class ImportExportService {

    private final EtudiantRepository etudiantRepository;
    private final ECRepository ecRepository;
    private final InscriptionRepository inscriptionRepository;
    private final EvaluationRepository evaluationRepository;
    private final UERepository ueRepository;

    @Autowired
    public ImportExportService(EtudiantRepository etudiantRepository,
                                ECRepository ecRepository,
                                InscriptionRepository inscriptionRepository,
                                EvaluationRepository evaluationRepository,
                                UERepository ueRepository) {
        this.etudiantRepository = etudiantRepository;
        this.ecRepository = ecRepository;
        this.inscriptionRepository = inscriptionRepository;
        this.evaluationRepository = evaluationRepository;
        this.ueRepository = ueRepository;
    }

    @Transactional
    public Map<String, Integer> importEtudiantsPourEC(MultipartFile fichier, String codeEC, String codeUE, int annee)
            throws IOException, CsvValidationException {

        // Variables compteur comme AtomicInteger car modifiées dans lambda
        AtomicInteger etudiantsCrees = new AtomicInteger(0);
        AtomicInteger inscriptionsCreees = new AtomicInteger(0);
        AtomicInteger evaluationsCreees = new AtomicInteger(0);

        // 1. Trouver ou créer l'EC
        EC ec = ecRepository.findByCodeEC(codeEC)
                .orElseGet(() -> {
                    EC newEC = new EC();
                    newEC.setCodeEC(codeEC);
                    newEC.setIntitule("À définir - " + codeEC);

                    UE defaultUE = ueRepository.findByCodeUE(codeUE)
                            .orElseGet(() -> {
                                UE ue = new UE();
                                ue.setCodeUE(codeUE);
                                ue.setIntitule("À définir - " + codeUE);
                                return ueRepository.save(ue);
                            });

                    newEC.setUe(defaultUE);
                    return ecRepository.save(newEC);
                });

        try (
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(fichier.getInputStream()))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build()
        ) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                // Ignore les lignes invalides ou incomplètes (maintenant seulement 3 colonnes nécessaires)
                if (line.length < 3 || line[0].isBlank() || line[1].isBlank() || line[2].isBlank()) continue;

                String matricule = line[0].trim();
                String nom = line[1].trim();
                String prenom = line[2].trim();

                Etudiant etudiant = etudiantRepository.findByMatricule(matricule)
                    .orElseGet(() -> {
                        Etudiant newEtudiant = new Etudiant();
                        newEtudiant.setMatricule(matricule);
                        newEtudiant.setNom(nom);
                        newEtudiant.setPrenom(prenom);
                        etudiantsCrees.incrementAndGet();  // Incrémenter ici sans redéclarer
                        return etudiantRepository.save(newEtudiant);
                    });

                // 3. Inscription
                Inscription inscription = inscriptionRepository
                        .findByEtudiantAndEcAndAnneeInscription(etudiant, ec, annee)
                        .orElseGet(() -> {
                            Inscription newInscription = new Inscription();
                            newInscription.setEtudiant(etudiant);
                            newInscription.setEc(ec);
                            newInscription.setAnneeInscription(annee);
                            inscriptionsCreees.incrementAndGet();
                            return inscriptionRepository.save(newInscription);
                        });

                // 4. Évaluation
//                if (evaluationRepository.findByInscription(inscription).isEmpty()) {
//                    Evaluation evaluation = new Evaluation();
//                    evaluation.setInscription(inscription);
//                    evaluation.setNoteControle(null);
//                    evaluation.setNoteExamen(null);
//                    evaluationRepository.save(evaluation);
//                    evaluationsCreees.incrementAndGet();
//                }
            }
        }

        // 5. Statistiques
        Map<String, Integer> stats = new HashMap<>();
        stats.put("etudiantsCrees", etudiantsCrees.get());
        stats.put("inscriptionsCreees", inscriptionsCreees.get());
        stats.put("evaluationsCreees", evaluationsCreees.get());
        return stats;
    }

    public String exportEvaluations() throws IOException {
        List<Evaluation> evaluations = evaluationRepository.findAll();

        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            String[] header = {"ID", "Matricule", "Nom", "Prénom", "Code EC", "Intitulé EC", "Année", "Note Contrôle", "Note Examen"};
            csvWriter.writeNext(header);

            for (Evaluation evaluation : evaluations) {
                String[] data = {
                        evaluation.getId().toString(),
                        evaluation.getInscription().getEtudiant().getMatricule(),
                        evaluation.getInscription().getEtudiant().getNom(),
                        evaluation.getInscription().getEtudiant().getPrenom(),
                        evaluation.getInscription().getEc().getCodeEC(),
                        evaluation.getInscription().getEc().getIntitule(),
                        evaluation.getInscription().getAnneeInscription().toString(),
                        evaluation.getNoteControle() != null ? evaluation.getNoteControle().toString() : "",
                        evaluation.getNoteExamen() != null ? evaluation.getNoteExamen().toString() : ""
                };
                csvWriter.writeNext(data);
            }
        }

        return stringWriter.toString();
    }
}