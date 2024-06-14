package org.sii.performance.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.service.api.BilanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/performance/bilan")
public class BilanController {

    private final BilanService bilanService;
    private static final Logger logger = LoggerFactory.getLogger(BilanController.class);

    @Autowired
    public BilanController(BilanService bilanService) {
        this.bilanService = bilanService;
    }

    @GetMapping("/entretien_annuel/{id_entretien_annuel}")
    public ResponseEntity<List<BilanDTO>> getAllBilan1sByEntretienAnnuel(@PathVariable Integer id_entretien_annuel) {
        logger.info("Received request to get all bilans by entretien annuel with ID: {}", id_entretien_annuel);
        return new ResponseEntity<>(bilanService.getAllBilansByEntretienAnnuel(id_entretien_annuel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BilanDTO> getBilan1ById(@PathVariable Integer id) {
        logger.info("Received request to get bilan by ID: {}", id);
        return new ResponseEntity<>(bilanService.getBilanById(id), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity deleteBilan1ById(@PathVariable Integer id){
        logger.info("Received request to delete bilan by ID: {}", id);
        bilanService.deleteBilanById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/user/add")
    public ResponseEntity<BilanDTO> addBilan1(@RequestBody BilanDTO bilanDTO) {
        logger.info("Received request to add a new bilan");
        BilanDTO bilan = bilanService.addBilan(bilanDTO);
        return new ResponseEntity<>(bilan, HttpStatus.CREATED);
    }

    @PutMapping("/user/update")
    public ResponseEntity<BilanDTO> updateBilan1(@RequestBody BilanDTO bilanDTO){
        logger.info("Received request to update bilan");
        BilanDTO bilan = bilanService.updateBilan(bilanDTO);
        return new ResponseEntity<>(bilan, HttpStatus.OK);
    }

    @PutMapping("/manager/addCommentaireManager")
    public ResponseEntity<BilanDTO> addCommentManager(@RequestBody BilanDTO bilanDTO){
        logger.info("Received request to add comment manager");
        return new ResponseEntity<>(bilanService.addCommentManager(bilanDTO), HttpStatus.OK);
    }
}
