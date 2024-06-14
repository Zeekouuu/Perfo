package org.sii.performance.mapper;

import org.sii.performance.dto.ObjectifDTO;
import org.sii.performance.entity.Objectif;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DTOToObjectifMapper implements Function<ObjectifDTO, Objectif> {
    @Override
    public  Objectif apply(ObjectifDTO objectifDTO) {
        Objectif objectif = new Objectif();
        objectif.setId(objectifDTO.getId());
        objectif.setNomObjectif(objectifDTO.getNomObjectif());
        objectif.setChargeActivites(objectifDTO.isChargeActivites());
        objectif.setSuiviFormation(objectifDTO.isSuiviFormation());
        objectif.setCommentaireCollab(objectifDTO.getCommentaireCollab());
        objectif.setCommentaireManager(objectifDTO.getCommentaireManager());
        objectif.setListFormation(objectifDTO.getListFormation());
        objectif.setDateCreation(objectifDTO.getDateCreation());
        return objectif;
    }


}

