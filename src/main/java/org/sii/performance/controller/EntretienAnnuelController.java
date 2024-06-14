package org.sii.performance.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseDTO;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseManagerDTO;
import org.sii.performance.dto.EntretienAnnuelDTO;
import org.sii.performance.entity.StatutEntretienAnnuel;
import org.sii.performance.service.api.EntretienAnnuelService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/performance/entretien_annuel")
public class EntretienAnnuelController {

    private EntretienAnnuelService entretienAnnuelService;
    private static final Logger logger = LoggerFactory.getLogger(EntretienAnnuelController.class);

    public EntretienAnnuelController(EntretienAnnuelService entretienAnnuelService) {
        this.entretienAnnuelService = entretienAnnuelService;
    }

    @GetMapping("/user")
    public ResponseEntity<List<EntretienAnnuelResponseDTO>> getEntretienAnnuelListByUser(@RequestHeader("id_user") Integer id) {
        logger.info("Received request to get entretien annuel list by user with ID: {}", id);
        List<EntretienAnnuelResponseDTO> entretienAnnuelListByUser = entretienAnnuelService.getEntretienAnnuelListByUser(id);
        return new ResponseEntity<>(entretienAnnuelListByUser, HttpStatus.OK);
    }

    @GetMapping("/manager")
    public ResponseEntity<List<EntretienAnnuelResponseManagerDTO>> getEntretienAnnuelListByManager(@RequestHeader("id_user") Integer id, @RequestParam StatutEntretienAnnuel statutEntretienAnnuel) {
        logger.info("Received request to get entretien annuel list by manager with ID: {} and statut: {}", id, statutEntretienAnnuel);
        List<EntretienAnnuelResponseManagerDTO> entretienAnnuelListByManager = entretienAnnuelService.getEntretienAnnuelListByManager(id, statutEntretienAnnuel);
        return new ResponseEntity<>(entretienAnnuelListByManager, HttpStatus.OK);
    }

    @PutMapping("/user/valider/{id}")
    public ResponseEntity  validerEntretienAnnuelByUser( @PathVariable Integer id) {
        logger.info("Received request to validate entretien annuel by user with ID: {}", id);
        entretienAnnuelService.validerEntretienAnnuelByUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/manager/valider/{id}")
    public ResponseEntity  validerEntretienAnnuelByManager(@PathVariable Integer id) {
        logger.info("Received request to validate entretien annuel by manager with ID: {}", id);
        entretienAnnuelService.validerEntretienAnnuelByManager(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> imprimerEntretienAnnuel(@RequestBody EntretienAnnuelDTO entretienAnnuel) throws IOException {
        logger.info("Received request to generate entretien annuel PDF");
        ByteArrayInputStream pdfStream = entretienAnnuelService.generatePdf(entretienAnnuel);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=entretien_annuel.pdf");

        try {
            byte[] pdfBytes = pdfStream.readAllBytes();
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            logger.error("Error occurred while generating entretien annuel PDF: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
