package org.sii.performance.repository;

import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.EntretienAnnuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BilanRepository extends JpaRepository<Bilan,Integer> {
    List<Bilan> findAllByEntretienAnnuel(EntretienAnnuel entretienAnnuel);
}
