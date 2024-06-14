package org.sii.performance.dto;

import lombok.*;
import org.sii.performance.entity.Bilan;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDTO {
    private Integer id;
    private  String nomAction;
    private String commentaireCollab;
    private String commentaireManager;
}