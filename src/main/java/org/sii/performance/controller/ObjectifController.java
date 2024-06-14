package org.sii.performance.controller;

import org.sii.performance.dto.ObjectifDTO;
import org.sii.performance.service.ObjectifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/performance/objectifs")
public class ObjectifController {

    private final ObjectifService objectifService;

    @Autowired
    public ObjectifController(ObjectifService objectifService) {
        this.objectifService = objectifService;
    }

    @GetMapping
    public ResponseEntity<List<ObjectifDTO>> getAllObjectifs() {
        List<ObjectifDTO> objectifs = objectifService.getAllObjectifs();
        return new ResponseEntity<>(objectifs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ObjectifDTO> getObjectifById(@PathVariable("id") Integer id) {
        ObjectifDTO objectif = objectifService.getObjectifById(id);
        if (objectif != null) {
            return new ResponseEntity<>(objectif, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ObjectifDTO> addObjectif(@RequestBody ObjectifDTO objectifDTO) {
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ObjectifDTO> updateObjectif(@PathVariable("id") Integer id, @RequestBody ObjectifDTO objectifDTO) {
        objectifDTO.setId(id);
        ObjectifDTO updatedObjectif = objectifService.updateObjectif(objectifDTO);
        if (updatedObjectif != null) {
            return new ResponseEntity<>(updatedObjectif, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteObjectif(@PathVariable("id") Integer id) {
        objectifService.deleteObjectif(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("objectifsByUserId/{userId}")
    public ResponseEntity<List<ObjectifDTO>> getObjectifsByUserId(@PathVariable Integer userId) {
        List<ObjectifDTO> objectifs = objectifService.getObjectifsByUserId(userId);
        return ResponseEntity.ok(objectifs);
    }
}
