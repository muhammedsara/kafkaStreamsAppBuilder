package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;

import java.util.List;

public interface PropertyRepository extends JpaRepository<PropertyModel,Integer> {

    List<PropertyModel> findPropertyModelByOperator_Id(Integer id);
}
