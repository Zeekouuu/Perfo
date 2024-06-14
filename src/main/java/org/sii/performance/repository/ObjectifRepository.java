package org.sii.performance.repository;

import org.sii.performance.entity.Bilan;
import org.sii.performance.entity.Objectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectifRepository extends JpaRepository<Objectif, Integer> {
    List<Objectif> getObjectifsByBilan(Bilan bilan);
    List<Objectif> findByBilanUserId(Integer userId);
}
