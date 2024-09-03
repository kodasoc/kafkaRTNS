package com.project.notification_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.notification_system.kafka.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class IntakeController {

    @Autowired
    PublisherService publisherService;

    @PostMapping("/intake")
    public ResponseEntity<Object> requestIntake(@RequestBody Object request) {
        try{
            publisherService.send(request);
            Thread.sleep(10000);
        }catch (JsonProcessingException e){
            log.error("Exception Occured", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<Object>(request, HttpStatus.OK);
    }

}
