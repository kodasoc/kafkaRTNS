package com.project.notification_system.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

    public Flux<String> consumeMessages() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .map(consumerRecord -> consumerRecord.value())
                .doOnNext(message -> System.out.println("Consumed message: " + message))
                .doOnError(e -> System.err.println("Error consuming message: " + e.getMessage()));
    }

    // Initialize the consumption process (e.g., on application startup)
    @PostConstruct
    public void startConsuming() {
        log.info("Consumer Started");
        consumeMessages()
                .subscribe();
    }
}
