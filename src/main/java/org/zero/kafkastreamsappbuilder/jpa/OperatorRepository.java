package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;

public interface OperatorRepository extends JpaRepository<OperatorModel,Integer> {
}
