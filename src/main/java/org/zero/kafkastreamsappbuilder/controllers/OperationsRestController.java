package org.zero.kafkastreamsappbuilder.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zero.kafkastreamsappbuilder.KafkastreamsappbuilderApplication;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.jpa.TypeRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;
import org.zero.kafkastreamsappbuilder.models.TypeModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class OperationsRestController {

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private TypeRepository typeRepository;
    /**
     * Get Available Operations on a Given Type.
     * @param sId id of source type.
     * @return list of available operations.
     */
    @RequestMapping("rest/getavailableoperations")
    public List<OperatorModel> getAvailableOperations(@RequestParam("sId") Integer sId){
        return operatorRepository.findOperatorModelBySourceType_Id(sId)
                .stream()
                .peek(x -> x.setProperties(null))
                .collect(Collectors.toList());
    }
}
