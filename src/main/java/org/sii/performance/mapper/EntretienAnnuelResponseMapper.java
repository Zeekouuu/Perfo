package org.sii.performance.mapper;


import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseDTO;
import org.sii.performance.entity.EntretienAnnuel;
import org.springframework.stereotype.Service;

import static fr.xebia.extras.selma.IoC.SPRING;
@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC =SPRING)
public interface EntretienAnnuelResponseMapper {
    EntretienAnnuelResponseDTO asEntretienAnnuelResponseDTO(EntretienAnnuel entretienAnnuel);
    EntretienAnnuel asEntretienAnnuel(EntretienAnnuelResponseDTO entretienAnnuelResponseDTO);
}
