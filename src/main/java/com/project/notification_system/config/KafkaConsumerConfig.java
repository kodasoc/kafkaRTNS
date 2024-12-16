package com.project.notification_system.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.*;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializer;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.topic}")
    private String topics; // Comma-separated list of topics

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;  // New Schema Registry URL

    @Value("${spring.kafka.properties.basic.auth.credentials.source}")
    private String authSource;

    @Value("${spring.kafka.properties.basic.auth.user.info}")
    private String userInfo;

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate() {
        // Split topics by comma and create a list
        List<String> topicList = Arrays.asList(topics.split(","));
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(getConsumerProps())
                .subscription(topicList); // Subscribe to multiple topics
        return new ReactiveKafkaConsumerTemplate<>(receiverOptions);
    }

    private Map<String, Object> getConsumerProps() {
        Map<String, Object> props = new HashMap<>();

        // Basic Kafka connection properties
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Adding security configurations for SASL_SSL
        props.put("security.protocol", securityProtocol);
        props.put("sasl.mechanism", saslMechanism);
        props.put("sasl.jaas.config", saslJaasConfig);

        // Optionally, if using schema registry, pass the schema registry URL
        if (schemaRegistryUrl != null && !schemaRegistryUrl.isEmpty()) {
            props.put("schema.registry.url", schemaRegistryUrl);
        }

        // Optional: If using basic authentication, add authentication details
        if (authSource != null && !authSource.isEmpty()) {
            props.put("basic.auth.credentials.source", authSource);
            props.put("basic.auth.user.info", userInfo);
        }

        return props;
    }
}
