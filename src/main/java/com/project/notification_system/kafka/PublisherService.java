package com.project.notification_system.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.kafka.sender.SenderResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
@Slf4j
@RequiredArgsConstructor
public class PublisherService {

    @Value("${spring.kafka.topic}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    // Working code
    public RecordMetadata send(Object message) {
        try {
            String messageString = new ObjectMapper().writeValueAsString(message);
            CompletableFuture<RecordMetadata> future =  reactiveKafkaProducerTemplate.send(topic, messageString)
                    .doOnError(e -> System.err.println("Error sending message: " + e.getMessage()))
                    .map(SenderResult::recordMetadata)
                    .doOnSuccess(metadata -> {
                        System.out.println("Message sent to topic: " + metadata.topic());
                        System.out.println("Partition: " + metadata.partition());
                        System.out.println("Offset: " + metadata.offset());
                        System.out.println("Timestamp: " + metadata.timestamp());
                    })
                    .toFuture();
            return future.get();
            // Blocking call to wait until message is published (hence this is synchronous now)
        }catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }


}}