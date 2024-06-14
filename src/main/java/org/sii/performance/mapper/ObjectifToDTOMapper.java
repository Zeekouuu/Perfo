package org.sii.performance.mapper;

import org.sii.performance.dto.ObjectifDTO;
import org.sii.performance.entity.Objectif;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ObjectifToDTOMapper implements Function<Objectif, ObjectifDTO> {

    @Override
    public ObjectifDTO apply(Objectif objectif) {
        return ObjectifDTO.builder()
                .id(objectif.getId())
                .nomObjectif(objectif.getNomObjectif())
                .chargeActivites(objectif.isChargeActivites())
                .suiviFormation(objectif.isSuiviFormation())
                .commentaireCollab(objectif.getCommentaireCollab())
                .commentaireManager(objectif.getCommentaireManager())
                .listFormation(objectif.getListFormation())
                .build();
    }
}