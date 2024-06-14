package org.sii.performance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sii.performance.dto.ObjectifDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.Objectif;
import org.sii.performance.entity.StatutBilan;
import org.sii.performance.exception.ObjectifNotFoundException;
import org.sii.performance.mapper.DTOToObjectifMapper;
import org.sii.performance.mapper.ObjectifToDTOMapper;
import org.sii.performance.repository.BilanRepository;
import org.sii.performance.repository.ObjectifRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectifServiceTest {

    @Mock
    private ObjectifRepository objectifRepository;
    @Mock
    private BilanRepository bilanRepository;
    @Mock
    private ObjectifToDTOMapper objectifToDTOMapper;
    @Mock
    private DTOToObjectifMapper dtoToObjectifMapper;
    @InjectMocks
    private ObjectifService objectifService;
    private ObjectifDTO testObjectifDTO;
    private Objectif testObjectif;

    @BeforeEach
    void setUp() {
        testObjectifDTO = ObjectifDTO.builder()
                .id(1)
                .nomObjectif("Test Objectif")
                .chargeActivites(true)
                .suiviFormation(false)
                .commentaireCollab("Test Comment")
                .commentaireManager("Test Manager Comment")
                .listFormation("Test Formation")
                .dateCreation(LocalDate.now())
                .build();

        testObjectif = new Objectif();
        testObjectif.setId(1);
        testObjectif.setNomObjectif("Test Objectif");
        testObjectif.setChargeActivites(true);
        testObjectif.setSuiviFormation(false);
        testObjectif.setCommentaireCollab("Test Comment");
        testObjectif.setCommentaireManager("Test Manager Comment");
        testObjectif.setListFormation("Test Formation");
        testObjectif.setDateCreation(LocalDate.now());
        testObjectif.setBilan(new Bilan());
    }



    private  Objectif objectif1 = new Objectif(1, "Objectif 1", true, false, "Commentaire collab 1", "Commentaire manager 1", "", LocalDate.now(), null);
    private  Objectif  objectif2 = new Objectif(2, "Objectif 2", false, true, "Commentaire collab 2", "Commentaire manager 2", "", LocalDate.now(), null);

    @Test
    void getAllObjectifs() {
        List<Objectif> mockObjectifList = List.of(objectif1,objectif2);
        when(objectifRepository.findAll()).thenReturn(mockObjectifList);

        List<ObjectifDTO> expected = mockObjectifList.stream()
                .map(objectifToDTOMapper::apply)
                .collect(Collectors.toList());

        List<ObjectifDTO> result = objectifService.getAllObjectifs();

        assertNotNull(result);
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);
    }

    @Test
    void getObjectifById() {
        Integer id = 1;
        when(objectifRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ObjectifNotFoundException.class, () -> objectifService.getObjectifById(id));
        verify(objectifRepository, times(1)).findById(id);
    }
    @Test
    public void addObjectifTest() {
        // Créer un objet Bilan
        Bilan bilan = new Bilan();
        bilan.setDateCreation(2024);
        bilan.setStatut(StatutBilan.A_VALIDER);
        bilan.setUserId(1);

        // Créer une liste d'ObjectifDTO
        List<ObjectifDTO> objectifDTOList = new ArrayList<>();
        objectifDTOList.add(new ObjectifDTO(1, "nomObjectif", false, false, "commentaireColab", "commentaireManager", "listFormation", LocalDate.now()));



        // Définir le comportement attendu pour dtoToObjectifMapper
        when(dtoToObjectifMapper.apply(any(ObjectifDTO.class)))
                .thenAnswer(invocation -> {
                    ObjectifDTO objectifDTO = invocation.getArgument(0);
                    Objectif objectif = new Objectif();
                    objectif.setId(objectifDTO.getId());
                    objectif.setNomObjectif(objectifDTO.getNomObjectif());
                    objectif.setChargeActivites(objectifDTO.isChargeActivites());
                    objectif.setSuiviFormation(objectifDTO.isSuiviFormation());
                    objectif.setCommentaireCollab(objectifDTO.getCommentaireCollab());
                    objectif.setCommentaireManager(objectifDTO.getCommentaireManager());
                    objectif.setListFormation(objectifDTO.getListFormation());
                    return objectif;
                });

        // Appeler la méthode à tester
        objectifService.addObjectif(objectifDTOList, bilan);

        // Vérifier que dtoToObjectifMapper.apply() a été appelé pour chaque ObjectifDTO
        verify(dtoToObjectifMapper, times(objectifDTOList.size())).apply(any(ObjectifDTO.class));

        // Vérifier que objectifRepository.save() a été appelé pour chaque Objectif
        verify(objectifRepository, times(objectifDTOList.size())).save(any(Objectif.class));
    }

    @Test
    void updateObjectif() {
        when(dtoToObjectifMapper.apply(testObjectifDTO)).thenReturn(testObjectif);
        when(objectifRepository.save(testObjectif)).thenReturn(testObjectif);
        when(objectifToDTOMapper.apply(testObjectif)).thenReturn(testObjectifDTO);

        ObjectifDTO updatedObjectifDTO = objectifService.updateObjectif(testObjectifDTO);

        assertNotNull(updatedObjectifDTO);
        assertEquals(testObjectifDTO.getId(), updatedObjectifDTO.getId());
        assertEquals(testObjectifDTO.getNomObjectif(), updatedObjectifDTO.getNomObjectif());
        assertEquals(testObjectifDTO.isChargeActivites(), updatedObjectifDTO.isChargeActivites());
        assertEquals(testObjectifDTO.isSuiviFormation(), updatedObjectifDTO.isSuiviFormation());
        assertEquals(testObjectifDTO.getCommentaireCollab(), updatedObjectifDTO.getCommentaireCollab());
        assertEquals(testObjectifDTO.getCommentaireManager(), updatedObjectifDTO.getCommentaireManager());
        assertEquals(testObjectifDTO.getListFormation(), updatedObjectifDTO.getListFormation());
        assertEquals(testObjectifDTO.getDateCreation(), updatedObjectifDTO.getDateCreation());
    }

    @Test
    void getObjectifsByBilan() {
        Integer bilanId = 1;
        Bilan bilan = new Bilan();
        bilan.setIdBilan(bilanId);

        List<Objectif> expectedObjectifs = new ArrayList<>();
        expectedObjectifs.add(new Objectif());
        expectedObjectifs.add(new Objectif());

        when(bilanRepository.findById(bilanId)).thenReturn(Optional.of(bilan));
        when(objectifRepository.getObjectifsByBilan(bilan)).thenReturn(expectedObjectifs);

        List<Objectif> actualObjectifs = objectifService.getObjectifsByBilan(bilanId);

        verify(bilanRepository).findById(bilanId);
        verify(objectifRepository).getObjectifsByBilan(bilan);
        assertEquals(expectedObjectifs.size(), actualObjectifs.size());
    }

    @Test
    void deleteObjectif() {
        Integer objectifId = 1;

        objectifService.deleteObjectif(objectifId);

        verify(objectifRepository).deleteById(objectifId);
    }

    @Test
    void getObjectifsByUserId() {
        Integer userId = 1;
        List<Objectif> objectifs = new ArrayList<>();
        objectifs.add(new Objectif());
        objectifs.add(new Objectif());

        List<ObjectifDTO> expectedObjectifDTOs = new ArrayList<>();
        expectedObjectifDTOs.add(new ObjectifDTO());
        expectedObjectifDTOs.add(new ObjectifDTO());

        when(objectifRepository.findByBilanUserId(userId)).thenReturn(objectifs);
        when(objectifToDTOMapper.apply(any())).thenReturn(new ObjectifDTO());

        // When
        List<ObjectifDTO> actualObjectifDTOs = objectifService.getObjectifsByUserId(userId);

        // Then
        verify(objectifRepository).findByBilanUserId(userId);
        verify(objectifToDTOMapper, times(objectifs.size())).apply(any());
        assertEquals(expectedObjectifDTOs.size(), actualObjectifDTOs.size());
    }


}