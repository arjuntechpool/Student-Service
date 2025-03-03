package com.student.detailsservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public boolean verifyStudentExists(String studentId) {
        try {
            // Call student service using Eureka service name
            String url = "http://student-service/students/verify/" + studentId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error verifying student: " + e.getMessage());
            // In production, use a proper logger
            return false;
        }
    }
}