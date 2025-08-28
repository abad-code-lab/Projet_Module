package com.uasz.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluation")
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "20.0")
    @Column(name = "note_controle")
    private Float noteControle;
    
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "20.0")
    @Column(name = "note_examen")
    private Float noteExamen;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inscription_id", unique = true, nullable = false)
    private Inscription inscription;
}