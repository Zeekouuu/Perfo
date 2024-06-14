package org.sii.performance.service;

import org.sii.performance.dto.ObjectifDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.Objectif;
import org.sii.performance.exception.ObjectifNotFoundException;
import org.sii.performance.mapper.ObjectifToDTOMapper;
import org.sii.performance.mapper.DTOToObjectifMapper;
import org.sii.performance.repository.BilanRepository;
import org.sii.performance.repository.ObjectifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObjectifService {

    private final ObjectifRepository objectifRepository;
    private final BilanRepository bilanRepository;
    private final ObjectifToDTOMapper objectifToDTOMapper;
    private final DTOToObjectifMapper dtoToObjectifMapper;

    @Autowired
    public ObjectifService(ObjectifRepository objectifRepository, BilanRepository bilanRepository, ObjectifToDTOMapper objectifToDTOMapper, DTOToObjectifMapper dtoToObjectifMapper) {
        this.objectifRepository = objectifRepository;
        this.bilanRepository = bilanRepository;
        this.objectifToDTOMapper = objectifToDTOMapper;
        this.dtoToObjectifMapper = dtoToObjectifMapper;
    }

    public List<ObjectifDTO> getAllObjectifs() {
        List<Objectif> objectifs = objectifRepository.findAll();
        return objectifs.stream()
                .map(objectifToDTOMapper)
                .collect(Collectors.toList());
    }

    public ObjectifDTO getObjectifById(Integer id) {
        return  objectifRepository.findById(id)
                .map(objectif -> objectifToDTOMapper.apply(objectif))
                .orElseThrow(()->new ObjectifNotFoundException());
    }

    public void addObjectif(List<ObjectifDTO> objectifs , Bilan bilan) {
        List<Objectif> objectifList = objectifs.stream()
                .map(objectifDTO -> dtoToObjectifMapper.apply(objectifDTO))
                .collect(Collectors.toList());
        for(Objectif objectif : objectifList){
            objectif.setBilan(bilan);
            objectifRepository.save(objectif);
        }
    }
    public ObjectifDTO updateObjectif(ObjectifDTO objectifDTO) {
        if (objectifDTO.getId() == null) {
            throw new IllegalArgumentException("Id cannot be null for update");
        }
        Objectif objectif = dtoToObjectifMapper.apply(objectifDTO);
        Objectif updatedObjectif = objectifRepository.save(objectif);
        return objectifToDTOMapper.apply(updatedObjectif);
    }

    public List<Objectif> getObjectifsByBilan(Integer id){
        Bilan bilan = bilanRepository.findById(id).get();
        return objectifRepository.getObjectifsByBilan(bilan);
    }

    public void deleteObjectif(Integer id) {
        objectifRepository.deleteById(id);
    }

    public List<ObjectifDTO> getObjectifsByUserId(Integer userId) {
        List<Objectif> objectifs = objectifRepository.findByBilanUserId(userId);
        return objectifs.stream()
                .map(objectif -> objectifToDTOMapper.apply(objectif))
                .collect(Collectors.toList());
    }


}
