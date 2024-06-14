package org.sii.performance.mapper;

import org.sii.performance.dto.ActionDTO;
import org.sii.performance.entity.Action;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ActionToDTOMapper implements Function<Action, ActionDTO> {
    @Override
    public ActionDTO apply(Action action) {
        return ActionDTO.builder()
                .id(action.getId())
                .nomAction(action.getNomAction())
                .commentaireCollab(action.getCommentaireCollab())
                .commentaireManager(action.getCommentaireManager())
                .build();
    }
}
