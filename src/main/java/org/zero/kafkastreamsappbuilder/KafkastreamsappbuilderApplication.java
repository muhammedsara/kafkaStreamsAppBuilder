package org.zero.kafkastreamsappbuilder;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.zero.kafkastreamsappbuilder.jpa.AppRepository;
import org.zero.kafkastreamsappbuilder.jpa.OperatorRepository;
import org.zero.kafkastreamsappbuilder.jpa.PropertyRepository;
import org.zero.kafkastreamsappbuilder.jpa.TypeRepository;
import org.zero.kafkastreamsappbuilder.models.AppModel;
import org.zero.kafkastreamsappbuilder.models.OperatorModel;
import org.zero.kafkastreamsappbuilder.models.PropertyModel;
import org.zero.kafkastreamsappbuilder.models.TypeModel;
import org.zero.kafkastreamsappbuilder.services.OperatorService;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@EnableJpaRepositories
public class KafkastreamsappbuilderApplication {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private OperatorRepository operatorRepository;
    @Autowired
    private OperatorService operatorService;

    public static void main(String[] args) {
        SpringApplication.run(KafkastreamsappbuilderApplication.class, args);
    }

    @Bean
    public OperatorService getOperatorService() {

        return new OperatorService();
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            if (appRepository.count() == 0) {
                // add default app
                AppModel app = new AppModel();
                app.setAppName("defaultApp");
                app.setCreateDate(new Date());
                appRepository.save(app);
            }
            if (typeRepository.count() == 0) {
                // add KStream type
                TypeModel kstream = new TypeModel();
                kstream.setTypeName("org.apache.kafka.streams.kstream.KStream");
                typeRepository.save(kstream);

                // add KTable type
                TypeModel ktable = new TypeModel();
                ktable.setTypeName("org.apache.kafka.streams.kstream.KTable");
                typeRepository.save(ktable);

                // add kgrouped type
                TypeModel kgrouped = new TypeModel();
                kgrouped.setTypeName("org.apache.kafka.streams.kstream.KGrouped");
                typeRepository.save(kgrouped);


                // kafka source operator
                operatorService.addOperator("kafka source",
                        "ksource.vm",
                        null,
                        kstream,
                        Arrays.asList("topicName", "keyType", "valueType"));

                // map operator
                operatorService.addOperator("map stream",
                        "map.vm",
                        kstream,
                        kstream,
                        Arrays.asList("mapExpression", "returnValueType"));

                //filter operator
                operatorService.addOperator("filter stream",
                        "filter.vm",
                        kstream,
                        kstream,
                        Arrays.asList("filterExpression"));

                //flatMapValues operator
                operatorService.addOperator("flatMapValues stream",
                        "flatMapValues.vm",
                        kstream,
                        kstream,
                        Arrays.asList("flatMapValuesExpression"));

                //groupByKey operator
                operatorService.addOperator("groupByKey stream",
                        "groupByKey.vm",
                        kstream,
                        kgrouped,
                        Arrays.asList(""));


                //count operator
                operatorService.addOperator("count stream",
                        "count.vm",
                        kgrouped,
                        ktable,
                        Arrays.asList(""));

                //mapValues operator
                operatorService.addOperator("mapValues stream",
                        "mapValues.vm",
                        ktable,
                        ktable,
                        Arrays.asList("mapValuesExpression"));

                //toStream operator
                operatorService.addOperator("toStream stream",
                        "toStream.vm",
                        ktable,
                        kstream,
                        Arrays.asList(""));

//                //groupBy operator
//                operatorService.addOperator("groupBy stream",
//                        "groupBy.vm",
//                        kstream,
//                        kstream,
//                        Arrays.asList("groupByExpression"));


            }
        };
    }
}

