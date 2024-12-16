package com.project.notification_system.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerService {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;
    private final Disposable consumeSubscription;


    public Flux<String> consumeMessages() {
        return reactiveKafkaConsumerTemplate
                .receiveAutoAck()
                .doOnNext(record -> System.out.println("Consumed message: " + record.value()))
                .flatMap(record-> processMessage((ReceiverRecord<String, String>) record))
                .doOnError(e -> System.err.println("Error consuming message: " + e.getMessage()));
    }


    private Flux<String> processMessage(ReceiverRecord<String, String> record) {
        String topic = record.topic();
        String message = record.value();

        // Custom processing based on the topic
        return switch (topic) {
            case "topic1" -> processTopic1(message);
            case "topic2" -> processTopic2(message);
            default -> processDefaultTopic(message);
        };
    }

    // Processing logic for topic1
    private Flux<String> processTopic1(String message) {
        System.out.println("Processing Topic1 message: " + message);
        return Flux.just(message);
    }

    // Processing logic for topic2
    private Flux<String> processTopic2(String message) {
        System.out.println("Processing Topic2 message: " + message);
        return Flux.just(message);
    }

    // Default processing logic
    private Flux<String> processDefaultTopic(String message) {
        System.out.println("Processing Default Topic message: " + message);
        // Default processing logic for other topics
        return Flux.just(message);
    }

    // Initialize the consumption process (e.g., on application startup)
    @PostConstruct
    public void startConsuming() {
        log.info("Consumer Started");
        consumeMessages()
                .subscribe();
    }

    // Delete pr clear the resources upon shutdown of service
    @PreDestroy
    public void stopConsuming() {
        if (consumeSubscription != null && !consumeSubscription.isDisposed()) {
            consumeSubscription.dispose();
        }
        log.info("Consumer Stopped");
    }
}
