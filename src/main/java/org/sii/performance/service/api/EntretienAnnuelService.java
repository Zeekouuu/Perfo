package org.sii.performance.service.api;


import org.sii.performance.callBack.entities.EntretienAnnuelResponseDTO;
import org.sii.performance.callBack.entities.EntretienAnnuelResponseManagerDTO;
import org.sii.performance.dto.EntretienAnnuelDTO;
import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.entity.StatutEntretienAnnuel;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

public interface EntretienAnnuelService {
    Optional<EntretienAnnuel> getOptionalEntretienAnnuelById(Integer id);
    List<EntretienAnnuelResponseDTO> getEntretienAnnuelListByUser(Integer id_user);
    List<EntretienAnnuelResponseManagerDTO> getEntretienAnnuelListByManager(Integer manager_id, StatutEntretienAnnuel statutEntretienAnnuel);
    void validerEntretienAnnuelByUser(Integer id);
    void validerEntretienAnnuelByManager(Integer id);
    ByteArrayInputStream generatePdf(EntretienAnnuelDTO entretienAnnuel) ;
}