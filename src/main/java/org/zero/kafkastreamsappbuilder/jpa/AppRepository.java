package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;

import javax.transaction.Transactional;

@Transactional
public interface AppRepository extends JpaRepository<AppModel,Integer> {
}
