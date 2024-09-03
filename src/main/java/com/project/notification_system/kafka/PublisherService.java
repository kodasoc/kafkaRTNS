package com.project.notification_system.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.shaded.com.google.protobuf.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherService {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    public Mono<Void> send(Object message) throws JsonProcessingException {
        String messageString = new ObjectMapper().writeValueAsString(message);
        return reactiveKafkaProducerTemplate.send(topic, messageString)
                .doOnSuccess(m-> log.info("Message Sent"))
                .doOnError(e -> log.error("Error While sending Message",e))
                .then();
    }


}