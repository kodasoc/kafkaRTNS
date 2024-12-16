package com.project.notification_system.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send-notification") // Endpoint for clients to send messages
    @SendTo("/topic/notifications") // Broadcast messages to all subscribers
    public String broadcastNotification(String message) {
        return message; // This will be broadcast to all subscribers
    }
}

