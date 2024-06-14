
package org.sii.performance.service;

import org.sii.performance.dto.ActionDTO;
import org.sii.performance.entity.Action;
import org.sii.performance.entity.Bilan;
import org.sii.performance.mapper.ActionToDTOMapper;
import org.sii.performance.mapper.DTOToActionMapper;
import org.sii.performance.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final ActionToDTOMapper actionToDTOMapper;
    private final DTOToActionMapper dtoToActionMapper;

    @Autowired
    public ActionService(ActionRepository actionRepository, ActionToDTOMapper actionToDTOMapper, DTOToActionMapper dtoToActionMapper) {
        this.actionRepository = actionRepository;
        this.actionToDTOMapper = actionToDTOMapper;
        this.dtoToActionMapper = dtoToActionMapper;
    }

    public List<ActionDTO> getAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream()
                .map(actionToDTOMapper)
                .collect(Collectors.toList());
    }

    public ActionDTO getActionById(Integer id) {
        Optional<Action> actionOptional = actionRepository.findById(id);
        return actionOptional.map(actionToDTOMapper::apply).orElse(null);
    }

    public void addAction(List<ActionDTO> actions , Bilan bilan) {
        List<Action> actionList = actions.stream()
                .map(actionDTO -> dtoToActionMapper.apply(actionDTO))
                .collect(Collectors.toList());
        for (Action action : actionList){
            action.setBilan(bilan);
            actionRepository.save(action);
        }
    }

     public ActionDTO updateAction(ActionDTO actionDTO) {
        if (actionDTO.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null for update");
        }
        Action action = dtoToActionMapper.apply(actionDTO);
        Action updatedAction = actionRepository.save(action);
        return actionToDTOMapper.apply(updatedAction);
    }

    public void deleteAction(Integer id) {
        actionRepository.deleteById(id);
    }

    public List<ActionDTO> getActionsByUserId(Integer userId) {
        List<Action> actions = actionRepository.findByBilanUserId(userId);
        return actions.stream()
                .map(action -> actionToDTOMapper.apply(action))
                .collect(Collectors.toList());
    }
}

