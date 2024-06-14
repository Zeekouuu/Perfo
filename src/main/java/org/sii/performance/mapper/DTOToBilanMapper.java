package org.sii.performance.mapper;

import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.Bilan;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DTOToBilanMapper implements Function<BilanDTO, Bilan> {
    private final DTOToObjectifMapper dtoToObjectifMapper;
    private final DTOToActionMapper dtoToActionMapper;

    public DTOToBilanMapper(DTOToObjectifMapper dtoToObjectifMapper, DTOToActionMapper dtoToActionMapper) {
        this.dtoToObjectifMapper = dtoToObjectifMapper;
        this.dtoToActionMapper = dtoToActionMapper;
    }

    @Override
    public Bilan apply(BilanDTO bilanDTO) {
        return Bilan.builder()
                .id(bilanDTO.getId())
                .dateCreation(bilanDTO.getDateCreation().getYear())
                .statut(bilanDTO.getStatut())
                .objectifs(bilanDTO.getObjectifs().stream().map(dtoToObjectifMapper::apply).collect(Collectors.toList()))
                .actions(bilanDTO.getActions().stream().map(dtoToActionMapper::apply).collect(Collectors.toList()))
                .userId(bilanDTO.getUserId())
                .build();
    }
}


