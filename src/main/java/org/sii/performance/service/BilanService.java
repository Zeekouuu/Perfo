package org.sii.performance.service;

import org.sii.performance.callBack.entities.UtilisateurDTO;
import org.sii.performance.callBack.services.EmailService;
import org.sii.performance.callBack.services.UserService;
import org.sii.performance.entity.Bilan;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.StatutBilan;
import org.sii.performance.mapper.BilanToDTOMapper;
import org.sii.performance.mapper.DTOToBilanMapper;
import org.sii.performance.repository.BilanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BilanService {

    private final BilanRepository bilanRepository;
    private final BilanToDTOMapper bilanToDTOMapper;
    private final DTOToBilanMapper dtoToBilanMapper;
    private final UserService userService;
    private  final EmailService emailService;
    private final ObjectifService objectifService;
    private final ActionService actionService;


    @Autowired
    public BilanService(BilanRepository bilanRepository, BilanToDTOMapper bilanToDTOMapper, DTOToBilanMapper dtoToBilanMapper, UserService userService, EmailService emailService, ObjectifService objectifService, ActionService actionService) {
        this.bilanRepository = bilanRepository;
        this.bilanToDTOMapper = bilanToDTOMapper;
        this.dtoToBilanMapper = dtoToBilanMapper;
        this.userService = userService;
        this.emailService = emailService;
        this.objectifService = objectifService;
        this.actionService = actionService;
    }

    public BilanDTO getBilanById(Integer id) {
        Optional<Bilan> bilanOptional = bilanRepository.findById(id);
        return bilanOptional.map(bilanToDTOMapper::apply).orElse(null);
    }

    public BilanDTO validerBilan(BilanDTO bilanDTO){
        Bilan bilan = dtoToBilanMapper.apply(bilanDTO);
        bilan.setStatut(StatutBilan.A_VALIDER);
        Bilan validateBilan = bilanRepository.save(bilan);
        UtilisateurDTO currentUser = userService.getUser(bilan.getUserId());
        String response = emailService.sendRequest(currentUser);
        return bilanToDTOMapper.apply(validateBilan);

    }

    private boolean checkIfBilanNotExist(Bilan bilan) {

        Optional<Bilan> optionalBilan = bilanRepository.findByDateCreationAndUserId(bilan.getDateCreation(), bilan.getUserId());
        if (optionalBilan.isPresent()){
            return false;
        }
        return true;

    }

    public BilanDTO addBilan(BilanDTO bilanDTO) {

        Bilan bilan = dtoToBilanMapper.apply(bilanDTO);
        if (bilan != null && checkIfBilanNotExist(bilan)) {
            Bilan savedBilan = bilanRepository.save(bilan);
            objectifService.addObjectif(bilanDTO.getObjectifs(), savedBilan);
            actionService.addAction(bilanDTO.getActions(), savedBilan);
            return bilanToDTOMapper.apply(savedBilan);
        }else{
            objectifService.addObjectif(bilanDTO.getObjectifs(),bilan);
            actionService.addAction(bilanDTO.getActions(),bilan);
            return bilanToDTOMapper.apply(bilan);
        }

    }


}