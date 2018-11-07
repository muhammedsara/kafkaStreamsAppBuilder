package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zero.kafkastreamsappbuilder.jpa.PropertyRepository;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PropertiesRestController {

    @Autowired
    private PropertyRepository propertyRepository;

    /**
     * Get Available Properties on a Given Type.
     * @param opId operator id
     * @return list of available properties of operator
     */
    @RequestMapping("rest/getavailableproperties")
    public List<PropertyModel> getAvailableProperties(@RequestParam("opId") Integer opId) {
        return propertyRepository.findPropertyModelByOperator_Id(opId)
                .stream()
                .peek(x -> x.setOperator(null))
                .collect(Collectors.toList());
    }
}
