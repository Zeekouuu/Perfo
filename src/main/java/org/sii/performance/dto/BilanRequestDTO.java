package org.sii.performance.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BilanRequestDTO {
    private List<ObjectifDTO> objectifs;
    private List<ActionDTO> actions;

    // Ajoutez les constructeurs, getters et setters nécessaires
}
