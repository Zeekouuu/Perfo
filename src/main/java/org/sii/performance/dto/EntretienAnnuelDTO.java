package org.sii.performance.dto;


import lombok.Data;
import org.sii.performance.callBack.entities.UtilisateurDTO;
import org.sii.performance.entity.StatutEntretienAnnuel;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
public class EntretienAnnuelDTO {
    private Integer id;
    private UtilisateurDTO user;
    private Integer managerId;
    private LocalDate dateCreation;
    @Enumerated(EnumType.STRING)
    private StatutEntretienAnnuel status;
}
