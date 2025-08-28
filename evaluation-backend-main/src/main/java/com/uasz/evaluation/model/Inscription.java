package com.uasz.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscription", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"annee_inscription", "etudiant_id", "ec_id"})
})
public class Inscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "annee_inscription", nullable = false)
    private Integer anneeInscription;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ec_id", nullable = false)
    private EC ec;
}