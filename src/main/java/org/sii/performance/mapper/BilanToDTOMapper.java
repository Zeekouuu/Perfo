package org.sii.performance.mapper;

import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.Bilan;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BilanToDTOMapper implements Function<Bilan,BilanDTO>{
    private final ObjectifToDTOMapper objectifToDTOMapper;
    private final ActionToDTOMapper actionToDTOMapper;

    public BilanToDTOMapper(ObjectifToDTOMapper objectifToDTOMapper, ActionToDTOMapper actionToDTOMapper) {
        this.objectifToDTOMapper = objectifToDTOMapper;
        this.actionToDTOMapper = actionToDTOMapper;
    }


    @Override
    public  BilanDTO apply(Bilan bilan) {
        return BilanDTO.builder()
                .id(bilan.getId())
                .dateCreation(LocalDate.ofEpochDay(bilan.getDateCreation()))
                .statut(bilan.getStatut())
                .objectifs(bilan.getObjectifs().stream().map(objectifToDTOMapper::apply).collect(Collectors.toList()))
                .actions(bilan.getActions().stream().map(actionToDTOMapper::apply).collect(Collectors.toList()))
                .userId(bilan.getUserId())
                .build();
    }
}
