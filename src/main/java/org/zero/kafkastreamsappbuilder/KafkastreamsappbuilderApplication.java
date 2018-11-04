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
import org.zero.kafkastreamsappbuilder.models.TypeModel;

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

	public static void main(String[] args) {
		SpringApplication.run(KafkastreamsappbuilderApplication.class, args);
	}

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            // add default app
            AppModel app = new AppModel();
            app.setAppName("defaultApp");
            app.setCreateDate(new Date());
            appRepository.save(app);

            // add KStream type
            TypeModel kstream = new TypeModel();
            kstream.setTypeName("org.apache.kafka.streams.kstream.KStream");
            typeRepository.save(kstream);

            // add KTable type
            TypeModel ktable = new TypeModel();
            ktable.setTypeName("org.apache.kafka.streams.kstream.KTable");
            typeRepository.save(ktable);

            // map operator
            OperatorModel map = new OperatorModel();
            map.setSourceType(kstream);
            map.setReturnType(kstream);
            map.setName("map stream");
            operatorRepository.save(map);
        };
    }
}

