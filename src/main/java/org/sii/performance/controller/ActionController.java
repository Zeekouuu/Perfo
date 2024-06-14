
package org.sii.performance.controller;

import org.sii.performance.dto.ActionDTO;
import org.sii.performance.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/performance/actions")
public class ActionController {

    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService) {
        this.actionService = actionService;
    }

    @GetMapping
    public ResponseEntity<List<ActionDTO>> getAllActions() {
        List<ActionDTO> actions = actionService.getAllActions();
        return new ResponseEntity<>(actions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActionDTO> getActionById(@PathVariable("id") Integer id) {
        ActionDTO action = actionService.getActionById(id);
        if (action != null) {
            return new ResponseEntity<>(action, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ActionDTO> addAction(@RequestBody ActionDTO actionDTO) {
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ActionDTO> updateAction(@PathVariable("id") Integer id, @RequestBody ActionDTO actionDTO) {
        actionDTO.setId(id);
        ActionDTO updatedAction = actionService.updateAction(actionDTO);
        if (updatedAction != null) {
            return new ResponseEntity<>(updatedAction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable("id") Integer id) {
        actionService.deleteAction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("actionsByUserId/{userId}")
    public ResponseEntity<List<ActionDTO>> getActionsByUserId(@PathVariable Integer userId) {
        List<ActionDTO> actions = actionService.getActionsByUserId(userId);
        return ResponseEntity.ok(actions);
    }
}

