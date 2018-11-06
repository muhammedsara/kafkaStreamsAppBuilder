package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;

import java.util.List;

public interface OperatorRepository extends JpaRepository<OperatorModel,Integer> {

    List<OperatorModel> findOperatorModelBySourceType_Id(Integer id);
}
