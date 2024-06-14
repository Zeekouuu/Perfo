package org.sii.performance;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sii.performance.callBack.entities.*;
import org.sii.performance.callBack.services.EmailService;
import org.sii.performance.callBack.services.UserService;
import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.entity.StatutEntretienAnnuel;
import org.sii.performance.exception.Constante;
import org.sii.performance.mapper.EntretienAnnuelResponseMapper;
import org.sii.performance.repository.EntretienAnnuelRepository;
import org.sii.performance.service.api.MessageHandler;
import org.sii.performance.service.impl.EntretienAnnuelServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EntretienAnnuelServiceImplTest {

    @Mock
    private EntretienAnnuelRepository entretienAnnuelRepository;

    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    @Mock
    private EntretienAnnuelResponseMapper entretienAnnuelResponseMapper;
    @Mock
    private MessageHandler messageHandler;


    @InjectMocks
    private EntretienAnnuelServiceImpl entretienAnnuelService;

    private UtilisateurDTO utilisateurDTO;
    private UtilisateurDTO manager;
    private EntretienAnnuel entretienAnnuel;
    private EntretienAnnuelResponseDTO annuelResponseDTO;
    private EntretienAnnuelResponseManagerDTO entretienAnnuelResponseManagerDTO;

    @BeforeEach
    void setUp() {

        manager = new UtilisateurDTO(
                1,
                "test",
                "test",
                "test.test@test.com",
                Role.MANAGER,
                null,
                false,
                20.0
        );
        utilisateurDTO = new UtilisateurDTO(
                3,
                "test",
                "test",
                "test.test@test.com",
                Role.USER,
                manager,
                false,
                20.0
        );
        entretienAnnuel = new EntretienAnnuel(
                1,
                3,
                1,
                LocalDate.of(2022,11,10),
                StatutEntretienAnnuel.BROUILLON,
                null);
        annuelResponseDTO = new EntretienAnnuelResponseDTO(
                1,
                utilisateurDTO,
                1,
                LocalDate.of(2022,11,10),
                StatutEntretienAnnuel.BROUILLON,
                null
        );
        entretienAnnuelResponseManagerDTO = new EntretienAnnuelResponseManagerDTO(
                annuelResponseDTO,
                LocalDate.of(2021,11,10)
        );
    }

    @Test
    void testGetOptionalEntretienAnnuelById() {
        // Préparation des données de test
        Integer entretienAnnuelId = 1;
        EntretienAnnuel entretienAnnuel = new EntretienAnnuel();
        // Configurez l'objet entretienAnnuel selon vos besoins

        // Configuration du mock
        when(entretienAnnuelRepository.findById(entretienAnnuelId)).thenReturn(Optional.of(entretienAnnuel));
        // Appel de la méthode à tester
        Optional<EntretienAnnuel> result = entretienAnnuelService.getOptionalEntretienAnnuelById(entretienAnnuelId);

        // Vérification
        assertTrue(result.isPresent()); // Vérifie que l'Optional contient une valeur
        assertEquals(entretienAnnuel, result.get()); // Vérifie que l'objet EntretienAnnuel retourné est celui attendu
    }

    @Test
    void testGetEntretienAnnuelListByUserTest() {
        // Préparation des données de test
        Integer userId = 3;
        List<EntretienAnnuel> entretienAnnuelList = List.of(entretienAnnuel);
        // Ajoutez des entretiens annuels à la liste entretienAnnuelList pour l'utilisateur spécifié

        // Configuration du mock du repository
        when(entretienAnnuelRepository.findAllyByUserId(userId)).thenReturn(entretienAnnuelList);
        when(entretienAnnuelResponseMapper.asEntretienAnnuelResponseDTO(entretienAnnuel)).thenReturn(annuelResponseDTO);
        // Configuration du mock du service utilisateur
        when(userService.getUser(userId)).thenReturn(utilisateurDTO);

        // Appel de la méthode à tester
        List<EntretienAnnuelResponseDTO> result = entretienAnnuelService.getEntretienAnnuelListByUser(userId);
        verify(userService, times(1)).getUser(anyInt());
        verify(entretienAnnuelResponseMapper, times(1)).asEntretienAnnuelResponseDTO(any());

        assertNotNull(result); // Vérifie que la liste retournée n'est pas nulle
        // Vérifiez d'autres aspects de la liste retournée selon vos besoins
    }
    @Test
    void testGetEntretienAnnuelListByManagerTest() {
        // Préparation des données de test
        Integer userId = 3;

        List<EntretienAnnuel> entretienAnnuelList = List.of(entretienAnnuel);
        // Ajoutez des entretiens annuels à la liste entretienAnnuelList pour l'utilisateur spécifié

        // Configuration du mock du repository
        when(entretienAnnuelRepository.findAllByManagerIdAndStatus(userId,StatutEntretienAnnuel.BROUILLON)).thenReturn(entretienAnnuelList);
        when(entretienAnnuelResponseMapper.asEntretienAnnuelResponseDTO(entretienAnnuel)).thenReturn(annuelResponseDTO);
        when(entretienAnnuelRepository.findTopByUserIdAndStatusOrderByDateCreationDesc(userId,StatutEntretienAnnuel.VALIDER)).thenReturn(Optional.of(entretienAnnuel));
        // Configuration du mock du service utilisateur
        when(userService.getUser(userId)).thenReturn(utilisateurDTO);

        // Appel de la méthode à tester
        List<EntretienAnnuelResponseManagerDTO> result = entretienAnnuelService.getEntretienAnnuelListByManager(userId,StatutEntretienAnnuel.BROUILLON);

        verify(userService, times(1)).getUser(anyInt());
        verify(entretienAnnuelResponseMapper, times(1)).asEntretienAnnuelResponseDTO(any());

        assertNotNull(result); // Vérifie que la liste retournée n'est pas nulle
        // Vérifiez d'autres aspects de la liste retournée selon vos besoins
    }
    @Test
    void ValiderEntretienAnnuelByUserTest() {
        // Préparation des données de test
        Integer idEntretienAnnuel = 1;
        Integer idCollab = 3;
        Integer idManager = 1;

        when(entretienAnnuelRepository.findById(idEntretienAnnuel)).thenReturn(Optional.of(entretienAnnuel));
        when(userService.getUser(idCollab)).thenReturn(utilisateurDTO);

        // Appel de la méthode à tester
        entretienAnnuelService.validerEntretienAnnuelByUser(idEntretienAnnuel);

        // Vérification des appels de méthode
        verify(entretienAnnuelRepository,times(2)).save(entretienAnnuel);
        verify(emailService).sendToManager(any(EmailDTO.class), eq(idCollab));
    }
    @Test
    void testGetEntretienAnnuel_EntityNotFoundException() {
        // Préparation des données de test
        Integer idEntretienAnnuel = 1;
        // Configurez le mock pour retourner le message attendu
        when(messageHandler.getMessage(Constante.ENTRETIEN_ANNUEL_NOT_FOUND_ERROR_MESSAGE))
                .thenReturn("Entretien annuel non trouvé.");

        when(entretienAnnuelRepository.findById(idEntretienAnnuel)).thenReturn(Optional.empty());

        // Appel de la méthode à tester et vérification de l'exception
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            entretienAnnuelService.getEntretienAnnuel(idEntretienAnnuel);
        });

        // Vérification du message de l'exception
        assertEquals("Entretien annuel non trouvé.", exception.getMessage());
    }
    @Test
    void CreateNewEntretienAnnuelTest() {
        // Préparation des données de test
        Integer userId = 1;

        // Capture d'argument pour vérifier les valeurs sauvegardées dans l'entretien annuel
        ArgumentCaptor<EntretienAnnuel> argumentCaptor = ArgumentCaptor.forClass(EntretienAnnuel.class);

        // Appel de la méthode à tester
        entretienAnnuelService.createNewEntretienAnnuel(userId);

        // Vérification que la méthode save a été appelée avec l'entretien annuel approprié
        verify(entretienAnnuelRepository).save(argumentCaptor.capture());

        // Récupération de l'entretien annuel capturé
        EntretienAnnuel savedEntretienAnnuel = argumentCaptor.getValue();

        // Assertions sur les valeurs de l'entretien annuel sauvegardé
        assertEquals(userId, savedEntretienAnnuel.getUserId());
        assertNull(savedEntretienAnnuel.getManagerId());
        assertEquals(StatutEntretienAnnuel.BROUILLON, savedEntretienAnnuel.getStatus());
        assertEquals(LocalDate.now(), savedEntretienAnnuel.getDateCreation());
    }
    @Test
    void ValiderEntretienAnnuelByManagerTest() {
        // Préparation des données de test
        Integer idEntretienAnnuel = 1;
        Integer idCollab = 3;
        Integer idManager = 1;

        when(entretienAnnuelRepository.findById(idEntretienAnnuel)).thenReturn(Optional.of(entretienAnnuel));
      //  when(userService.getUser(idCollab)).thenReturn(utilisateurDTO);

        // Appel de la méthode à tester
        entretienAnnuelService.validerEntretienAnnuelByManager(idEntretienAnnuel);

        // Vérification des appels de méthode
        verify(entretienAnnuelRepository).save(entretienAnnuel);
    }



}
