package org.sii.performance.repository;

import org.sii.performance.entity.EntretienAnnuel;
import org.sii.performance.entity.StatutEntretienAnnuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntretienAnnuelRepository extends JpaRepository<EntretienAnnuel, Integer> {
    Optional<EntretienAnnuel> findTopByUserIdAndStatusOrderByDateCreationDesc(Integer userId, StatutEntretienAnnuel status);
    List<EntretienAnnuel> findAllyByUserId(Integer id_user);
    List<EntretienAnnuel> findAllByManagerIdAndStatus(Integer ManagerId, StatutEntretienAnnuel statutEntretienAnnuel);
}
