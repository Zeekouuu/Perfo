package org.sii.performance.dto;

import lombok.Data;
import org.sii.performance.entity.BilanType;
@Data
public class BilanDTO {
    private Integer id;
    private String descriptive;
    private String intitule;
    private String commentaireManager;
    private BilanType bilanType;
    private EntretienAnnuelDTO entretienAnnuel;
}
