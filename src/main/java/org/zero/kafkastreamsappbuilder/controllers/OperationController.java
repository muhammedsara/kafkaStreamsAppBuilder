package org.zero.kafkastreamsappbuilder.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zero.kafkastreamsappbuilder.exceptions.NoAppFoundException;
import org.zero.kafkastreamsappbuilder.exceptions.NoOperatorFoundException;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.jpa.PropertyRepository;
import org.zero.kafkastreamsappbuilder.jpa.TypeRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;
import org.zero.kafkastreamsappbuilder.models.TypeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class OperationController {

    @Autowired
    public OperatorRepository operatorRepository;
    @Autowired
    public PropertyRepository propertyRepository;
    @Autowired
    public TypeRepository typeRepository;

    @RequestMapping(value = "/operators", method = RequestMethod.GET)
    public String operators(Map<String, Object> model) {
        List<OperatorModel> all = operatorRepository.findAll();
        model.put("operators", all);
        return "fragments/operators";
    }

    @RequestMapping(value = "/operator/create", method = RequestMethod.GET)
    public String newOperator(Map<String, Object> model) {
        List<TypeModel> all = typeRepository.findAll();
        model.put("types", all);
        model.put("operator", new OperatorModel());
        return "fragments/create_operator";
    }

    @RequestMapping(value = "/operator/edit/{id}", method = RequestMethod.GET)
    public String editOperator(Map<String, Object> model,
                               @PathVariable Integer id) {
        Optional<OperatorModel> operator = operatorRepository.findById(id);
        if (operator.isPresent()) {
            model.put("operator", operator.get());
            List<TypeModel> all = typeRepository.findAll();
            model.put("types", all);
        } else {
            throw new NoOperatorFoundException();
        }
        return "fragments/create_operator";
    }

    @PostMapping(value = "/operator/save")
    public String saveOperator(@ModelAttribute OperatorModel model) {
        // TODO: why manual cascading?
        List<PropertyModel> modifiedProperties = new ArrayList<>();
        List<PropertyModel> oldProperties = propertyRepository.findPropertyModelByOperator_Id(model.getId());
        oldProperties.forEach(p -> propertyRepository.delete(p));
        for (PropertyModel m : model.getProperties()) {
            m.setOperator(model);
            modifiedProperties.add(propertyRepository.save(m));
        }
        model.setProperties(modifiedProperties);
        operatorRepository.save(model);
        return "fragments/success";
    }

}
