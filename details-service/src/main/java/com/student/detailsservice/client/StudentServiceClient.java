package com.student.detailsservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentServiceClient {

    private static final String STUDENT_SERVICE = "studentService";

    @Autowired
    private RestTemplate restTemplate;

    // This is correct - keep this as is
    @CircuitBreaker(name = STUDENT_SERVICE, fallbackMethod = "verifyStudentExistsFallback")
    @Retry(name = STUDENT_SERVICE)
    public boolean verifyStudentExists(String studentId) {
        try {
            String url = "http://student-service/students/verify/" + studentId;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            System.err.println("Error verifying student: " + e.getMessage());
            throw e;
        }
    }

    // Remove any circuit breaker annotations from the fallback method
    public boolean verifyStudentExistsFallback(String studentId, Exception e) {
        System.err.println("Fallback for verifyStudentExists: " + e.getMessage());
        return true;
    }
}