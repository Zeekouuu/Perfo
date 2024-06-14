package org.sii.performance.repository;

import org.sii.performance.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionRepository extends JpaRepository<Action,Integer> {

    List<Action>  findByBilanUserId(Integer userId);
}
