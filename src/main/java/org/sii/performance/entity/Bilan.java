package org.sii.performance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bilan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descriptive;
    private String intitule;
    private String commentaireManager;
    @Enumerated(EnumType.STRING)
    private BilanType bilanType;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_entretien_annuel")
    private EntretienAnnuel entretienAnnuel;
}
