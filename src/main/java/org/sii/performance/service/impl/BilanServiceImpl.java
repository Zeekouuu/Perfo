package org.sii.performance.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.exception.Constante;
import org.sii.performance.mapper.BilanMapper;
import org.sii.performance.repository.BilanRepository;
import org.sii.performance.service.api.BilanService;
import org.sii.performance.service.api.EntretienAnnuelService;
import org.sii.performance.service.api.MessageHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BilanServiceImpl implements BilanService {

    private final BilanRepository bilanRepository;
    private final EntretienAnnuelService entretienAnnuelService;
    private final BilanMapper bilanMapper;
    private final MessageHandler messageHandler;
    private static final Logger logger = LoggerFactory.getLogger(BilanServiceImpl.class);

    @Override
    public List<BilanDTO> getAllBilansByEntretienAnnuel(Integer id_entretien_annuel) {
        logger.info("Received request to get all bilans by entretien annuel with ID: {}", id_entretien_annuel);
        Optional<EntretienAnnuel> optionalEntretienAnnuel = entretienAnnuelService.getOptionalEntretienAnnuelById(id_entretien_annuel);
        List<BilanDTO> bilanDTOList = new ArrayList<>();
        optionalEntretienAnnuel.ifPresent(entretienAnnuel -> bilanRepository.findAllByEntretienAnnuel(entretienAnnuel)
                .stream()
                .map(bilanMapper::asBilanDTO)
                .collect(Collectors.toCollection(() -> bilanDTOList)));
        return bilanDTOList;
    }

    public Bilan getBilan(Integer id) {
        logger.info("Received request to get bilan by ID: {}", id);
        return bilanRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                messageHandler.getMessage(
                                        Constante.BILAN1_NOT_FOUND_ERROR_MESSAGE)
                        )
                );
    }

    @Override
    public BilanDTO getBilanById(Integer id) {
        logger.info("Received request to get bilan DTO by ID: {}", id);
        Bilan bilan = getBilan(id);
        BilanDTO bilanDTO = bilanMapper.asBilanDTO(bilan);
        return bilanDTO;
    }

    @Override
    public BilanDTO addBilan(BilanDTO bilanDTO) {
        logger.info("Received request to add bilan");
        Bilan bilan = bilanMapper.asBilan(bilanDTO);
        EntretienAnnuel entretienAnnuel = entretienAnnuelService.getOptionalEntretienAnnuelById(bilanDTO.getEntretienAnnuel().getId()).orElse(null);
        bilan.setEntretienAnnuel(entretienAnnuel);
        return bilanMapper.asBilanDTO(bilanRepository.save(bilan));
    }

    @Override
    public BilanDTO updateBilan(BilanDTO bilanDTO) {
        logger.info("Received request to update bilan");
        Bilan bilan = getBilan(bilanDTO.getId());
        bilan.setBilanType(bilanDTO.getBilanType());
        bilan.setIntitule(bilanDTO.getIntitule());
        bilan.setDescriptive(bilanDTO.getDescriptive());
        Bilan saved = bilanRepository.save(bilan);
        return bilanMapper.asBilanDTO(saved);
    }

    @Override
    public void deleteBilanById(Integer id) {
        logger.info("Received request to delete bilan by ID: {}", id);
        Bilan bilan = getBilan(id);
        bilanRepository.delete(bilan);
    }

    @Override
    public BilanDTO addCommentManager(BilanDTO bilanDTO) {
        logger.info("Received request to add comment manager to bilan");
        Bilan bilan = getBilan(bilanDTO.getId());
        bilan.setCommentaireManager(bilanDTO.getCommentaireManager());
        return bilanMapper.asBilanDTO(bilanRepository.save(bilan));
    }
}