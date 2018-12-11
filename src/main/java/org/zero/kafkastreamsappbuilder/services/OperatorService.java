package org.zero.kafkastreamsappbuilder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.jpa.PropertyRepository;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;
import org.zero.kafkastreamsappbuilder.models.TypeModel;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class OperatorService {

    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private OperatorRepository operatorRepository;

    public void addOperator(String operatorName,
                            String templateFile,
                            TypeModel sourceType,
                            TypeModel returnType,
                            List<String> properties)
            throws IOException {
        OperatorModel operator = new OperatorModel();
        operator.setSourceType(sourceType);
        operator.setReturnType(returnType);
        operator.setTemplate(
                new String(
                        Files.readAllBytes(
                                new ClassPathResource("velocity/"+ templateFile)
                                        .getFile()
                                        .toPath()
                        )
                )
        );
        operator.setName(operatorName);
        operatorRepository.save(operator);
        for(String propertyName: properties){
            PropertyModel propertyModel = new PropertyModel();
            propertyModel.setName(propertyName);
            propertyModel.setOperator(operator);
            propertyRepository.save(propertyModel);
        }
    }

}
