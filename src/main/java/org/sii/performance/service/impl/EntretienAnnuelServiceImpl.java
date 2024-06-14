package org.sii.performance.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import lombok.AllArgsConstructor;
import org.sii.performance.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sii.performance.dto.EntretienAnnuelDTO;
import org.sii.performance.service.api.EntretienAnnuelService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.awt.Color;
import org.sii.performance.callBack.entities.EmailDTO;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseDTO;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseManagerDTO;
import org.sii.performance.callBack.entities.UtilisateurDTO;
import org.sii.performance.callBack.services.EmailService;
import org.sii.performance.callBack.services.UserService;
import org.sii.performance.constant.EmailMessages;
import org.sii.performance.constant.EmailUrl;
import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.exception.Constante;
import org.sii.performance.mapper.BilanMapper;
import org.sii.performance.mapper.EntretienAnnuelResponseMapper;
import org.sii.performance.repository.EntretienAnnuelRepository;
import org.sii.performance.service.api.MessageHandler;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EntretienAnnuelServiceImpl implements EntretienAnnuelService {

    private final EntretienAnnuelRepository entretienAnnuelRepository;
    private final EntretienAnnuelResponseMapper entretienAnnuelResponseMapper;
    private final MessageHandler messageHandler;
    private final UserService userService;
    private final EmailService emailService;
    private final BilanMapper bilanMapper;
    private static final Logger logger = LoggerFactory.getLogger(EntretienAnnuelServiceImpl.class);

    @Override
    public Optional<EntretienAnnuel> getOptionalEntretienAnnuelById(Integer id) {
        logger.info("Received request to get optional entretien annuel by ID: {}", id);
        return entretienAnnuelRepository.findById(id);
    }

    @Override
    public List<EntretienAnnuelResponseDTO> getEntretienAnnuelListByUser(Integer id_user) {
        logger.info("Received request to get entretien annuel list by user ID: {}", id_user);
        List<EntretienAnnuelResponseDTO> entretienAnnuelResponseDTOList = new ArrayList<>();
        entretienAnnuelRepository.findAllyByUserId(id_user)
                .stream()
                .map(entretienAnnuel -> {
                    EntretienAnnuelResponseDTO entretienAnnuelResponseDTO = entretienAnnuelResponseMapper.asEntretienAnnuelResponseDTO(entretienAnnuel);
                    UtilisateurDTO user = userService.getUser(entretienAnnuel.getUserId());
                    entretienAnnuelResponseDTO.setUser(user);
                    return entretienAnnuelResponseDTO;
                })
                .collect(Collectors.toCollection(() -> entretienAnnuelResponseDTOList));
        return entretienAnnuelResponseDTOList;
    }

    @Override
    public List<EntretienAnnuelResponseManagerDTO> getEntretienAnnuelListByManager(Integer id_manager, StatutEntretienAnnuel statutEntretienAnnuel) {
        logger.info("Received request to get entretien annuel list by manager ID: {} and status: {}", id_manager, statutEntretienAnnuel);
        List<EntretienAnnuelResponseDTO> entretienAnnuelResponseDTOList = new ArrayList<>();
        entretienAnnuelRepository.findAllByManagerIdAndStatus(id_manager, statutEntretienAnnuel)
                .stream()
                .map(entretienAnnuel -> {
                    EntretienAnnuelResponseDTO entretienAnnuelResponseDTO = entretienAnnuelResponseMapper.asEntretienAnnuelResponseDTO(entretienAnnuel);
                    UtilisateurDTO user = userService.getUser(entretienAnnuel.getUserId());
                    entretienAnnuelResponseDTO.setUser(user);
                    return entretienAnnuelResponseDTO;
                })
                .collect(Collectors.toCollection(() -> entretienAnnuelResponseDTOList));
        List<EntretienAnnuelResponseManagerDTO> entretienAnnuelList = getEntretienAnnuelList(entretienAnnuelResponseDTOList);

        return entretienAnnuelList;
    }

    private List<EntretienAnnuelResponseManagerDTO> getEntretienAnnuelList(List<EntretienAnnuelResponseDTO> entretienAnnuelResponseDTOList) {
        List<EntretienAnnuelResponseManagerDTO> annuelResponseManagerDTOList = new ArrayList<>();
        for (EntretienAnnuelResponseDTO entretien : entretienAnnuelResponseDTOList) {
            Optional<EntretienAnnuel> OptionalAncienEntretienAnnuel = entretienAnnuelRepository.findTopByUserIdAndStatusOrderByDateCreationDesc(entretien.getUser().id(), StatutEntretienAnnuel.valueOf("VALIDER"));
            if (OptionalAncienEntretienAnnuel.isPresent()){
                EntretienAnnuel ancienEntretien = OptionalAncienEntretienAnnuel.get();
                EntretienAnnuelResponseManagerDTO managerDTO = new EntretienAnnuelResponseManagerDTO(entretien, ancienEntretien.getDateCreation());
                annuelResponseManagerDTOList.add(managerDTO);
            }else {
                EntretienAnnuelResponseManagerDTO managerDTO = new EntretienAnnuelResponseManagerDTO(entretien, null);
                annuelResponseManagerDTOList.add(managerDTO);
            }
        }
        return annuelResponseManagerDTOList;
    }

    @Override
    public void validerEntretienAnnuelByUser(Integer id) {
        logger.info("Received request to validate entretien annuel by user with ID: {}", id);
        addManagerToEntretienAnnuel(id, getEntretienAnnuel(id).getUserId());
        validerEntretienAnnuelWithStatus(id, StatutEntretienAnnuel.A_VALIDER);
        EmailDTO emailDTO = new EmailDTO(
                getEntretienAnnuel(id).getManagerId(), EmailMessages.validateEntretien,
                true,
                EmailUrl.performanceRequestPage);
        emailService.sendToManager(emailDTO, getEntretienAnnuel(id).getUserId());
    }

    private void addManagerToEntretienAnnuel(Integer id_entretien_anneul, Integer id_collab) {
        EntretienAnnuel entretienAnnuel = getEntretienAnnuel(id_entretien_anneul);
        UtilisateurDTO collab = userService.getUser(id_collab);
        entretienAnnuel.setManagerId(collab.manager().id());
        entretienAnnuelRepository.save(entretienAnnuel);
    }




    @Transactional
    @Override
    public void validerEntretienAnnuelByManager(Integer id) {
        logger.info("Received request to validate entretien annuel by manager with ID: {}", id);
        validerEntretienAnnuelWithStatus(id, StatutEntretienAnnuel.VALIDER);
        EmailDTO emailDTO = new EmailDTO(
                getEntretienAnnuel(id).getUserId(),
                EmailMessages.confirmEntretien,
                true,
                EmailUrl.performancePage);
        emailService.sendToCollab(emailDTO, getEntretienAnnuel(id).getManagerId());
        createNewEntretienAnnuel(getEntretienAnnuel(id).getUserId());
    }

    public void validerEntretienAnnuelWithStatus(Integer id, StatutEntretienAnnuel statutEntretienAnnuel) {
        logger.info("Received request to validate entretien annuel with ID: {} and status: {}", id, statutEntretienAnnuel);
        EntretienAnnuel entretienAnnuel = getEntretienAnnuel(id);
        entretienAnnuel.setStatus(statutEntretienAnnuel);
        entretienAnnuelRepository.save(entretienAnnuel);
    }

    public void createNewEntretienAnnuel(Integer userId) {
        logger.info("Received request to create new entretien annuel for user with ID: {}", userId);
        EntretienAnnuel entretienAnnuel = new EntretienAnnuel();
        entretienAnnuel.setUserId(userId);
        entretienAnnuel.setManagerId(null);
        entretienAnnuel.setStatus(StatutEntretienAnnuel.BROUILLON);
        entretienAnnuel.setDateCreation(LocalDate.now());

        entretienAnnuelRepository.save(entretienAnnuel);
    }


    public EntretienAnnuel getEntretienAnnuel(Integer id) {
        logger.info("Received request to get entretien annuel with ID: {}", id);
        return entretienAnnuelRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                messageHandler.getMessage(
                                        Constante.ENTRETIEN_ANNUEL_NOT_FOUND_ERROR_MESSAGE)
                        )
                );
    }
    @Override
    public ByteArrayInputStream generatePdf(EntretienAnnuelDTO entretienAnnuelDTO) {
        logger.info("Received request to generate PDF for entretien annuel with ID: {}", entretienAnnuelDTO.getId());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
       EntretienAnnuel entretienAnnuel = getEntretienAnnuel(entretienAnnuelDTO.getId());
       UtilisateurDTO manager = userService.getUser(entretienAnnuelDTO.getManagerId());
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Paragraph title = new Paragraph("SII ENTRETIENT ANNUEL", new Font(Font.HELVETICA, 20, Font.BOLD, new Color(0, 128, 255)));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            ClassPathResource resource = new ClassPathResource("static/image.png");
            InputStream logoStream = resource.getInputStream();
            Image logo = Image.getInstance(logoStream.readAllBytes());
            logo.scaleAbsolute(40, 40);


            float marginRight = 20;
            float marginTop = 20;
            logo.setAbsolutePosition(document.getPageSize().getWidth() - 40 - marginRight,
                    document.getPageSize().getHeight() - 40 - marginTop);

            document.add(logo);

            document.add(Chunk.NEWLINE);

            // Ajouter le premier tableau avec les informations de base
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(20f);
            infoTable.setSpacingAfter(20f);

            addStyledCell(infoTable, "Nom du collaborateur", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(infoTable, entretienAnnuelDTO.getUser().firstName() +" "+entretienAnnuelDTO.getUser().lastName(), Color.LIGHT_GRAY, Color.BLACK);
            addStyledCell(infoTable, "Nom du manager", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(infoTable, manager.firstName()+" "+manager.lastName(), Color.LIGHT_GRAY, Color.BLACK);
            addStyledCell(infoTable, "Date de création", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(infoTable, entretienAnnuel.getDateCreation().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), Color.LIGHT_GRAY, Color.BLACK);
            addStyledCell(infoTable, "Statut", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(infoTable, entretienAnnuel.getStatus().toString(), Color.LIGHT_GRAY, Color.BLACK);

            document.add(infoTable);

            // Ajouter le deuxième tableau avec les bilans
            PdfPTable bilanTable1 = new PdfPTable(2);
            bilanTable1.setWidthPercentage(100);
            bilanTable1.setSpacingBefore(20f);

            addStyledCell(bilanTable1, "Intitulé", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(bilanTable1, "Commentaire Manager", new Color(0, 128, 255), Color.WHITE);
            Paragraph paragraph1 = new Paragraph("Actions", new Font(Font.HELVETICA, 15, Font.BOLD, new Color(0, 128, 255)));
            document.add(paragraph1);
            for (Bilan bilan : entretienAnnuel.getBilan()) {
                if(bilan.getBilanType().equals(BilanType.ACTION)){
                    addStyledCell(bilanTable1, bilan.getIntitule(), Color.LIGHT_GRAY, Color.BLACK);
                    addStyledCell(bilanTable1, bilan.getCommentaireManager(), Color.LIGHT_GRAY, Color.BLACK);
                }
            }
            document.add(bilanTable1);
            PdfPTable bilanTable2 = new PdfPTable(2);
            bilanTable2.setWidthPercentage(100);
            bilanTable2.setSpacingBefore(20f);
            addStyledCell(bilanTable2, "Intitulé", new Color(0, 128, 255), Color.WHITE);
            addStyledCell(bilanTable2, "Commentaire Manager", new Color(0, 128, 255), Color.WHITE);
            Paragraph paragraph2 = new Paragraph("Objectifs", new Font(Font.HELVETICA, 15, Font.BOLD, new Color(0, 128, 255)));
            document.add(paragraph2);
            for (Bilan bilan : entretienAnnuel.getBilan()) {
                if(bilan.getBilanType().equals(BilanType.OBJECTIF)){
                    addStyledCell(bilanTable2, bilan.getIntitule(), Color.LIGHT_GRAY, Color.BLACK);
                    addStyledCell(bilanTable2, bilan.getCommentaireManager(), Color.LIGHT_GRAY, Color.BLACK);
                }
            }

            document.add(bilanTable2);

            document.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Ajouter une cellule stylisée avec les couleurs de fond et de police spécifiées
    private void addStyledCell(PdfPTable table, String content, Color backgroundColor, Color fontColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, new Font(Font.HELVETICA, 12, Font.NORMAL, fontColor)));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        table.addCell(cell);
    }

}



