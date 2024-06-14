package org.sii.performance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sii.performance.dto.BilanDTO;
import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.StatutBilan;
import org.sii.performance.mapper.BilanToDTOMapper;
import org.sii.performance.mapper.DTOToBilanMapper;
import org.sii.performance.repository.BilanRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BilanServiceTest {

    @Mock
    private BilanRepository bilanRepository;

    @Mock
    private BilanToDTOMapper bilanToDTOMapper;

    @Mock
    private DTOToBilanMapper dtoToBilanMapper;

    @Mock
    private ObjectifService objectifService;

    @Mock
    private ActionService actionService;

    @InjectMocks
    private BilanService bilanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBilanById() {

        Integer id = 1;
        BilanDTO expectedBilanDTO = createBilanDTO();
        Bilan expectedBilan = createBilan();

        when(bilanRepository.findById(id)).thenReturn(Optional.of(expectedBilan));
        when(bilanToDTOMapper.apply(expectedBilan)).thenReturn(expectedBilanDTO);

        BilanDTO actualBilanDTO = bilanService.getBilanById(id);

        assertNotNull(actualBilanDTO);
        assertEquals(expectedBilanDTO, actualBilanDTO);
        verify(bilanRepository, times(1)).findById(id);
        verify(bilanToDTOMapper, times(1)).apply(expectedBilan);
    }

    @Test
    public void addBilanTest() {
        // Création d'un Bilan fictif
        Bilan bilan = createBilan();

        // Mocks
        when(dtoToBilanMapper.apply(any(BilanDTO.class))).thenReturn(bilan);
        when(bilanRepository.save(any(Bilan.class))).thenReturn(bilan);

        // Input BilanDTO
        BilanDTO bilanDTO = createBilanDTO();

        // Execution
        BilanDTO result = bilanService.addBilan(bilanDTO);

        // Verification
        verify(objectifService, never()).addObjectif(anyList(), any());
        verify(actionService, never()).addAction(anyList(), any());
    }


    private BilanDTO createBilanDTO() {
        return BilanDTO.builder()
                .idBilan(1)
                .statut(StatutBilan.A_VALIDER)
                .userId(123)
                .dateCreation(LocalDate.now())
                .build();
    }

    private Bilan createBilan() {
        return Bilan.builder()
                .idBilan(1)
                .statut(StatutBilan.A_VALIDER)
                .userId(123)
                .dateCreation(LocalDate.now().getYear())
                .build();
    }
}