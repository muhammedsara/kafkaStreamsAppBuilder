package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;

public interface PropertyRepository extends JpaRepository<PropertyModel,Integer> {
}
