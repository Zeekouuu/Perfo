package org.sii.performance.service.api;

import org.sii.performance.dto.BilanDTO;
import java.util.List;

public interface BilanService {
    List<BilanDTO> getAllBilansByEntretienAnnuel(Integer id_entretien_annuel);
    BilanDTO getBilanById(Integer id);
    BilanDTO addBilan(BilanDTO bilanDTO);
    BilanDTO updateBilan(BilanDTO bilanDTO);
    void deleteBilanById(Integer id);
    BilanDTO addCommentManager(BilanDTO bilanDTO);
}
