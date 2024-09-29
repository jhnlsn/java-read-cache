package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@Configuration
public class EmbeddedKafkaConfig {

    @Bean
    public EmbeddedKafkaBroker embeddedKafkaBroker() {
        return new EmbeddedKafkaBroker(1, false, 1, "parameter-topic")
                .brokerProperty("listeners", "PLAINTEXT://localhost:9092")
                .brokerProperty("port", "9092");
    }
}
