package org.zero.kafkastreamsappbuilder.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;

import java.util.List;

@RestController
public class OperationsRestController {

    @Autowired
    private OperatorRepository operatorRepository;

    /**
     * Get Available Operations on a Given Type.
     * @param sId id of source type.
     * @return list of available operations.
     */
    @RequestMapping("rest/getavailableoperations")
    public List<OperatorModel> getAvailableOperations(@RequestParam("sId") Integer sId){
        return operatorRepository.findOperatorModelBySourceType_Id(sId);
    }

}
