package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zero.kafkastreamsappbuilder.exceptions.NoAppFoundException;
import org.zero.kafkastreamsappbuilder.exceptions.NoOperatorFoundException;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OperationController {

    @Autowired
    public OperatorRepository operatorRepository;

    @RequestMapping(value = "/operators", method = RequestMethod.GET)
    public String operators(Map<String, Object> model) {
        List<OperatorModel> all = operatorRepository.findAll();
        model.put("operators", all);
        return "fragments/operators";
    }
    @RequestMapping(value = "/operator/create", method = RequestMethod.GET)
    public String newOperator(Map<String, Object> model) {
        return "fragments/create_operator";
    }

    @RequestMapping(value = "/operator/edit/{id}", method = RequestMethod.GET)
    public String editOperator(Map<String, Object> model,
                          @PathVariable Integer id) {
        Optional<OperatorModel> operator = operatorRepository.findById(id);
        if (operator.isPresent()) {
            model.put("operator", operator.get());
        } else {
            throw new NoOperatorFoundException();
        }
        return "fragments/create_operator";
    }
}
