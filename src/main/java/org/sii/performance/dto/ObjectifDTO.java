package org.sii.performance.dto;
import lombok.*;
import org.sii.performance.entity.Bilan;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectifDTO {
    private Integer id;
    private String nomObjectif;
    private boolean chargeActivites;
    private boolean suiviFormation;
    private String commentaireCollab;
    private String commentaireManager;
    private String listFormation;
    private LocalDate dateCreation;
}
