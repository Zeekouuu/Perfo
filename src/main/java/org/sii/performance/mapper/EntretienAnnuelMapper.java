package org.sii.performance.mapper;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import org.sii.performance.dto.EntretienAnnuelDTO;
import org.sii.performance.entity.EntretienAnnuel;
import org.springframework.stereotype.Service;

import static fr.xebia.extras.selma.IoC.SPRING;
@Service
@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC =SPRING)
public interface EntretienAnnuelMapper {
    EntretienAnnuelDTO asEntretienAnnuelDTO(EntretienAnnuel entretienAnnuel);
    EntretienAnnuel asEntretienAnnuel(EntretienAnnuelDTO entretienAnnuelDTO);
}
