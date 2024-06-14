package org.sii.performance.callBack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.StatutEntretienAnnuel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntretienAnnuelResponseDTO {
    private  Integer id;
    private UtilisateurDTO user;
    private Integer managerId;
    private LocalDate dateCreation;
    @Enumerated(EnumType.STRING)
    private StatutEntretienAnnuel status;
    private List<BilanDTO> bilan;
}
