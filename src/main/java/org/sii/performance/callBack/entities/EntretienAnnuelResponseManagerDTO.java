package org.sii.performance.callBack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntretienAnnuelResponseManagerDTO {

    private EntretienAnnuelResponseDTO entretienAnnuelResponseDTO;

    private LocalDate dateAncienCreation;

}
