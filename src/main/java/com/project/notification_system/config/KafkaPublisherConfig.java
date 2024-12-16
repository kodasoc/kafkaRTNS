package com.project.notification_system.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaPublisherConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;  // Fixed typo

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;  // Add security protocol for SASL_SSL if required

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;  // Add SASL mechanism for authentication

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;  // Add SASL JAAS configuration for authentication

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate() {
        SenderOptions<String, String> senderOptions = SenderOptions.<String, String>create(getProducerProps());
        return new ReactiveKafkaProducerTemplate<>(senderOptions);
    }

    // Define the producer properties method
    private Map<String, Object> getProducerProps() {
        // Create a map to hold the Kafka producer properties
        Map<String, Object> props = new HashMap<>();

        // Basic producer properties
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        props.put(ProducerConfig.ACKS_CONFIG, "1");  // Consider changing to "all" for durability

        // Security properties for SASL_SSL (if required)
        if (securityProtocol != null && !securityProtocol.isEmpty()) {
            props.put("security.protocol", securityProtocol);
        }
        if (saslMechanism != null && !saslMechanism.isEmpty()) {
            props.put("sasl.mechanism", saslMechanism);
        }
        if (saslJaasConfig != null && !saslJaasConfig.isEmpty()) {
            props.put("sasl.jaas.config", saslJaasConfig);
        }

        return props;
    }
}
