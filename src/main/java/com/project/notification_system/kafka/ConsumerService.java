package com.project.notification_system.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.Disposable;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
    private Disposable consumeSubscription;
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void startConsuming() {
        log.info("Consumer Started");

        // Start consuming messages
        consumeSubscription = reactiveKafkaConsumerTemplate.receive()
                .doOnNext(record -> log.info("Consumed message: " + record.value()))  // Use record directly (no casting)
                .flatMap(this::processMessage)  // Process each message
                .doOnError(e -> log.error("Error consuming message: " + e.getMessage()))
                .subscribe();  // Subscribe to the Flux to start consuming messages
    }

    private Flux<String> processMessage(ReceiverRecord<String, String> record) {
        String topic = record.topic();
        String message = record.value();
        // Process based on topic
        switch (topic) {
            case "topic1":
                return processTopic1(message);
            case "topic2":
                return processTopic2(message);
            default:
                return processDefaultTopic(message);
        }
    }

    private Flux<String> processTopic1(String message) {
        log.info("Processing Topic1 message: " + message);
        messagingTemplate.convertAndSend("/topic/notifications", message);
        return Flux.just(message);
    }

    private Flux<String> processTopic2(String message) {
        log.info("Processing Topic2 message: " + message);
        return Flux.just(message);
    }

    private Flux<String> processDefaultTopic(String message) {
        log.info("Processing Default Topic message: " + message);
        return Flux.just(message);
    }

    @PreDestroy
    public void stopConsuming() {
        if (consumeSubscription != null && !consumeSubscription.isDisposed()) {
            consumeSubscription.dispose();
        }
        log.info("Consumer Stopped");
    }
}
