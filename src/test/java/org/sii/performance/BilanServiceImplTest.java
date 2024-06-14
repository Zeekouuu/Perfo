package org.sii.performance;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.dto.EntretienAnnuelDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.BilanType;
import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.exception.Constante;
import org.sii.performance.mapper.BilanMapper;
import org.sii.performance.repository.BilanRepository;
import org.sii.performance.service.impl.BilanServiceImpl;
import org.sii.performance.service.api.EntretienAnnuelService;
import org.sii.performance.service.api.MessageHandler;

import javax.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class BilanServiceImplTest {
    @Mock
    private BilanRepository bilanRepository;
    @Mock
    private EntretienAnnuelService entretienAnnuelService;
    @Mock
    private BilanMapper bilanMapper;

    @Mock
    private MessageHandler messageHandler;

    @InjectMocks
    private BilanServiceImpl bilanService;

    @Test
    void getAllBilansByEntretienAnnuelTest() {
        // Préparation des données
        Integer entretienAnnuelId = 1;
        Bilan bilan1 = new Bilan(1,"descriptive","intitule","commentaireManager1", BilanType.ACTION,null);
        Bilan bilan2 = new Bilan(2,"descriptive2","intilile2","commentaireManager2",BilanType.OBJECTIF,null);
        List<Bilan> bilans = List.of(bilan1,bilan2);
        BilanDTO bilanDTO = new BilanDTO();
        bilanDTO.setId(1);
        EntretienAnnuel entretienAnnuel = new EntretienAnnuel();
        entretienAnnuel.setId(entretienAnnuelId);
        Optional<EntretienAnnuel> optionalEntretienAnnuel = Optional.of(entretienAnnuel);

        when(entretienAnnuelService.getOptionalEntretienAnnuelById(entretienAnnuelId)).thenReturn(optionalEntretienAnnuel);
        when(bilanRepository.findAllByEntretienAnnuel(entretienAnnuel)).thenReturn(bilans);
        when(bilanMapper.asBilanDTO(any())).thenReturn(bilanDTO);

        // Appel de la méthode à tester
        List<BilanDTO> result = bilanService.getAllBilansByEntretienAnnuel(entretienAnnuelId);

        // Vérification
        // Assurez-vous que la méthode getOptionalEntretienAnnuelById a été appelée avec l'ID attendu
        verify(entretienAnnuelService).getOptionalEntretienAnnuelById(entretienAnnuelId);
        // Assurez-vous que la méthode findAllByEntretienAnnuel a été appelée avec l'entretien annuel correspondant
        verify(bilanRepository).findAllByEntretienAnnuel(entretienAnnuel);
        // Assurez-vous que la méthode asBilanDTO a été appelée pour chaque bilan
        verify(bilanMapper, times(bilans.size())).asBilanDTO(any());
        // Assurez-vous que la méthode retourne la liste de DTOs attendue
        assertEquals(2, result.size());
        // Assurez-vous que le résultat contient le DTO attendu
        assertEquals(bilanDTO, result.get(0));

    }

    @Test
    void getBilanExistingBilanTest() {
        // Préparation des données
        Integer bilanId = 1;
        Bilan expectedBilan = new Bilan();
        when(bilanRepository.findById(bilanId)).thenReturn(Optional.of(expectedBilan));

        // Appel de la méthode à tester
        Bilan result = bilanService.getBilan(bilanId);

        // Vérification
        // Assurez-vous que findById() a été appelé avec l'ID attendu
        verify(bilanRepository).findById(bilanId);
        // Assurez-vous que le résultat retourné est l'entité Bilan attendue
        assertEquals(expectedBilan, result);
    }

    @Test
    void getBilanNonExistingBilanTest() {
        Integer bilanId = 1;
        when(bilanRepository.findById(bilanId)).thenReturn(Optional.empty());
        when(messageHandler.getMessage(Constante.BILAN1_NOT_FOUND_ERROR_MESSAGE)).thenReturn("Bilan non trouvé");

        // Vérification si la méthode lance bien une EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> bilanService.getBilan(bilanId));

        // Assurez-vous que findById() a été appelé avec l'ID attendu
        verify(bilanRepository).findById(bilanId);
        // Assurez-vous que getMessage() a été appelé avec le message d'erreur attendu
        verify(messageHandler).getMessage(Constante.BILAN1_NOT_FOUND_ERROR_MESSAGE);
    }
    @Test
    void getBilanByIdTest() {
        // Préparation des données
        Integer bilanId = 1;
        Bilan bilan = new Bilan();
        BilanDTO expectedDTO = new BilanDTO();

        when(bilanMapper.asBilanDTO(bilan)).thenReturn(expectedDTO);
        when(bilanRepository.findById(bilanId)).thenReturn(Optional.of(bilan));

        // Appel de la méthode à tester
        BilanDTO result = bilanService.getBilanById(bilanId);

        // Vérification
        // Assurez-vous que asBilanDTO() a été appelé avec le bilan obtenu
        verify(bilanMapper).asBilanDTO(bilan);
        // Assurez-vous que le résultat retourné est le DTO attendu
        assertEquals(expectedDTO, result);
    }

    @Test
    void addBilanTest() {
        // Préparation des données
        BilanDTO bilanDTO = new BilanDTO();
        Bilan bilan = new Bilan();
        EntretienAnnuel entretienAnnuel = new EntretienAnnuel();
        bilanDTO.setEntretienAnnuel(new EntretienAnnuelDTO());
        when(bilanMapper.asBilan(bilanDTO)).thenReturn(bilan);
        when(entretienAnnuelService.getOptionalEntretienAnnuelById(bilanDTO.getEntretienAnnuel().getId())).thenReturn(Optional.of(entretienAnnuel));
        when(bilanRepository.save(bilan)).thenReturn(bilan);
        when(bilanMapper.asBilanDTO(bilan)).thenReturn(bilanDTO);

        // Appel de la méthode à tester
        BilanDTO result = bilanService.addBilan(bilanDTO);

        // Vérification
        verify(bilanMapper).asBilan(bilanDTO);
        // Assurez-vous que getOptionalEntretienAnnuelById() a été appelé avec l'ID d'entretien annuel de bilanDTO
        verify(entretienAnnuelService).getOptionalEntretienAnnuelById(bilanDTO.getEntretienAnnuel().getId());
        // Assurez-vous que setEntretienAnnuel() a été appelé avec l'entretien annuel obtenu
       // verify(bilan).setEntretienAnnuel(entretienAnnuel);
        // Assurez-vous que save() a été appelé avec le bilan
        verify(bilanRepository).save(bilan);
        // Assurez-vous que asBilanDTO() a été appelé avec le bilan sauvegardé
        verify(bilanMapper).asBilanDTO(bilan);
        // Assurez-vous que le résultat retourné est le DTO attendu
        assertEquals(bilanDTO, result);
    }

    @Test
    void updateBilanTest() {
        // Préparation des données
        BilanDTO bilanDTO = new BilanDTO();
        bilanDTO.setId(1);
        Bilan bilan = new Bilan();
        Bilan updatedBilan = new Bilan();
        when(bilanRepository.findById(anyInt())).thenReturn(Optional.of(bilan));
        when(bilanRepository.save(bilan)).thenReturn(updatedBilan);
        when(bilanMapper.asBilanDTO(updatedBilan)).thenReturn(bilanDTO);

        // Appel de la méthode à tester
        BilanDTO result = bilanService.updateBilan(bilanDTO);

        // Vérification
        // Assurez-vous que save() a été appelé avec le bilan mis à jour
        verify(bilanRepository).save(bilan);
        // Assurez-vous que asBilanDTO() a été appelé avec le bilan mis à jour
        verify(bilanMapper).asBilanDTO(updatedBilan);
        // Assurez-vous que le résultat retourné est le DTO attendu
        assertEquals(bilanDTO, result);
    }
    @Test
    void deleteBilanByIdTest() {
        // Préparation des données
        Integer id = 1;
        Bilan bilan = new Bilan();
        when(bilanRepository.findById(id)).thenReturn(Optional.of(bilan));

        bilanService.deleteBilanById(id);

        verify(bilanRepository).delete(bilan);
    }
    @Test
    void addCommentManagerTest() {
        // Préparation des données
        BilanDTO bilanDTO = new BilanDTO();
        bilanDTO.setId(1);
        String commentaireManager = "Test commentaire";
        bilanDTO.setCommentaireManager(commentaireManager);
        Bilan bilan = new Bilan();
        when(bilanRepository.findById(anyInt())).thenReturn(Optional.of(bilan));
        when(bilanMapper.asBilanDTO(bilan)).thenReturn(bilanDTO);
        when(bilanRepository.save(bilan)).thenReturn(bilan); // Utilisation de lenient

        // Appel de la méthode à tester
        BilanDTO result = bilanService.addCommentManager(bilanDTO);

        // Vérification
        verify(bilanRepository).save(bilan);
        assertEquals(commentaireManager, bilan.getCommentaireManager());
        assertEquals(bilanDTO, result);
    }

}
