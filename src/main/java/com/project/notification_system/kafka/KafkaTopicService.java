package com.project.notification_system.kafka;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaTopicService {

    private final AdminClient adminClient;

    @PostConstruct
    public void createTopic() {
        NewTopic topic = new NewTopic("notification-topics", 1, (short) 1);
        CreateTopicsResult result = adminClient.createTopics(Collections.singleton(topic));
        try {
            result.all().get();
            System.out.println("Topic created successfully");
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }
}
