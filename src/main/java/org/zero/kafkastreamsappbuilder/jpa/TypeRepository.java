package org.zero.kafkastreamsappbuilder.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zero.kafkastreamsappbuilder.models.TypeModel;

public interface TypeRepository extends JpaRepository<TypeModel,Integer> {
}
