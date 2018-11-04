package org.zero.kafkastreamsappbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class KafkastreamsappbuilderApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkastreamsappbuilderApplication.class, args);
	}
}
