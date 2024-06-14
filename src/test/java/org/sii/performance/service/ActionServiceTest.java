package org.sii.performance.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sii.performance.dto.ActionDTO;
import org.sii.performance.entity.Action;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.StatutBilan;
import org.sii.performance.mapper.ActionToDTOMapper;
import org.sii.performance.mapper.DTOToActionMapper;
import org.sii.performance.repository.ActionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private ActionToDTOMapper actionToDTOMapper;

    @Mock
    private DTOToActionMapper dtoToActionMapper;

    @InjectMocks
    private ActionService actionService;
    @Test
    void getAllActions() {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action());
        when(actionRepository.findAll()).thenReturn(actions);

        actionService.getAllActions();

        verify(actionRepository, times(1)).findAll();
    }

    @Test
    void getActionById() {

        Integer id = 1;
        Action action = new Action();
        when(actionRepository.findById(id)).thenReturn(Optional.of(action));

        actionService.getActionById(id);

        verify(actionRepository, times(1)).findById(id);
    }

    @Test
    public void addActionTest() {
        Bilan bilan1 = new Bilan();
        bilan1.setDateCreation(2024);
        bilan1.setStatut(StatutBilan.A_VALIDER);
        bilan1.setUserId(1);

        List<ActionDTO> actionDTOList = new ArrayList<>();
        actionDTOList.add(new ActionDTO(1, "Action 1", "Commentaire pour Action 1","commentaireManager"));
        actionDTOList.add(new ActionDTO(2, "Action 2", "Commentaire pour Action 2","commentaireManager"));



        when(dtoToActionMapper.apply(any(ActionDTO.class)))
                .thenAnswer(invocation -> {
                    ActionDTO actionDTO = invocation.getArgument(0);
                    Action action = new Action();
                    action.setId(actionDTO.getId());
                    action.setNomAction(actionDTO.getNomAction());
                    action.setCommentaireCollab(actionDTO.getCommentaireCollab());
                    action.setCommentaireManager(actionDTO.getCommentaireManager());
                    return action;
                });


        actionService.addAction(actionDTOList, bilan1);

        verify(dtoToActionMapper, times(actionDTOList.size())).apply(any(ActionDTO.class));

        verify(actionRepository, times(actionDTOList.size())).save(any(Action.class));
    }

    @Test
    public void testUpdateAction_WithNonNullId() {

        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setId(1);

        Action action = new Action();
        action.setId(1);

        when(dtoToActionMapper.apply(actionDTO)).thenReturn(action);
        when(actionRepository.save(action)).thenReturn(action);
        when(actionToDTOMapper.apply(action)).thenReturn(actionDTO);

        ActionDTO updatedActionDTO = actionService.updateAction(actionDTO);


        assertNotNull(updatedActionDTO);
        assertEquals(actionDTO, updatedActionDTO);
        verify(actionRepository, times(1)).save(action);
    }

    @Test
    public void testUpdateAction_WithNullId() {

        ActionDTO actionDTO = new ActionDTO(); // Id is null by default


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            actionService.updateAction(actionDTO);
        });


        assertEquals("Id cannot be null for update", exception.getMessage());
        verify(actionRepository, never()).save(any());
    }

    @Test
    void deleteAction() {
        Integer id = 1;

        actionService.deleteAction(id);

        verify(actionRepository, times(1)).deleteById(id);
    }

    @Test
    void getActionsByUserId() {

        Integer userId = 1;
        List<Action> actions = new ArrayList<>();
        actions.add(new Action());
        when(actionRepository.findByBilanUserId(userId)).thenReturn(actions);

        ActionDTO actionDTO = new ActionDTO();
        when(actionToDTOMapper.apply(any(Action.class))).thenReturn(actionDTO);

        List<ActionDTO> result = actionService.getActionsByUserId(userId);

        verify(actionRepository, times(1)).findByBilanUserId(userId);
        verify(actionToDTOMapper, times(actions.size())).apply(any(Action.class));
    }
}