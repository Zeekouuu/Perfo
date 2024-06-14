package org.sii.performance.mapper;

import org.sii.performance.dto.ActionDTO;
import org.sii.performance.entity.Action;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DTOToActionMapper implements Function<ActionDTO, Action> {

    @Override
    public Action apply(ActionDTO actionDTO) {
        Action action = new Action();
        action.setId(actionDTO.getId());
        action.setNomAction(actionDTO.getNomAction());
        action.setCommentaireCollab(actionDTO.getCommentaireCollab());
        action.setCommentaireManager(actionDTO.getCommentaireManager());
        return action;
    }
}
