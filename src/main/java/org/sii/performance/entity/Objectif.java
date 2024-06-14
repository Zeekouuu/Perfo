package org.sii.performance.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objectif {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nomObjectif;
    private boolean chargeActivites;
    private boolean suiviFormation;
    private String commentaireCollab;
    private String commentaireManager;
    private String listFormation;
    private LocalDate dateCreation;
    @ManyToOne
    @JoinColumn(name = "id_bilan")
    private Bilan bilan;
}