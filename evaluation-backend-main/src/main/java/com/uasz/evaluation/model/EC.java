package com.uasz.evaluation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ec")
public class EC {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(unique = true, nullable = false)
    private String codeEC;
    
    @Column(nullable = false)
    private String intitule;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ue_id", nullable = false)
    private UE ue;
}