package com.project.notification_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.notification_system.controller.IntakeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationSystemApplication {

	@Autowired
    static IntakeController intakeController;

static String p= "{\"strin\": \"yrs\"};";

	public static void main(String[] args) {
		SpringApplication.run(NotificationSystemApplication.class, args);

				NotificationSystemApplication.intakeController.requestIntake(new ObjectMapper().convertValue(p,Object.class));
	}

}
