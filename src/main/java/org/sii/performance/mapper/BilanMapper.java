package org.sii.performance.mapper;

import fr.xebia.extras.selma.IgnoreMissing;
import fr.xebia.extras.selma.Mapper;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.mapper.EntretienAnnuelMapper;
import org.springframework.stereotype.Service;

import static fr.xebia.extras.selma.IoC.SPRING;
@Service
@Mapper(withIgnoreMissing = IgnoreMissing.ALL, withIoC = SPRING, withCustom = EntretienAnnuelMapper.class)
public interface BilanMapper {
    BilanDTO asBilanDTO(Bilan bilan);
    Bilan asBilan(BilanDTO bilanDTO);
}
