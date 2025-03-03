package com.student.studentservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentDetailsClient {

    private static final String DETAILS_SERVICE = "detailsService";

    @Autowired
    private RestTemplate restTemplate;

    // Add circuit breaker to this method
    @CircuitBreaker(name = DETAILS_SERVICE, fallbackMethod = "createStudentDetailsFallback")
    @Retry(name = DETAILS_SERVICE)
    public void createStudentDetails(String username, String token, String firstName, String lastName, String email) {
        String url = "http://student-details-service/details";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("X-User", username);

        StudentDetailsDto detailsDto = new StudentDetailsDto();
        detailsDto.setStudentId(username);
        detailsDto.setFirstName(firstName);
        detailsDto.setLastName(lastName);
        detailsDto.setEmail(email);

        HttpEntity<StudentDetailsDto> entity = new HttpEntity<>(detailsDto, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    // Add the fallback method for createStudentDetails
    public void createStudentDetailsFallback(String username, String token, String firstName,
                                             String lastName, String email, Exception e) {
        System.err.println("Fallback for createStudentDetails: " + e.getMessage());
        // Maybe log this for later retry or queue it
    }

    // Add circuit breaker to this method
    @CircuitBreaker(name = DETAILS_SERVICE, fallbackMethod = "getStudentDetailsFallback")
    @Retry(name = DETAILS_SERVICE)
    public StudentDetailsDto getStudentDetails(String username, String token) {
        String url = "http://student-details-service/details/" + username;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("X-User", username);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                StudentDetailsDto.class
        ).getBody();
    }

    // Add the fallback method for getStudentDetails
    public StudentDetailsDto getStudentDetailsFallback(String username, String token, Exception e) {
        System.err.println("Fallback for getStudentDetails: " + e.getMessage());
        // Return a default/empty StudentDetailsDto
        StudentDetailsDto defaultDto = new StudentDetailsDto();
        defaultDto.setStudentId(username);
        defaultDto.setFirstName("Unavailable");
        defaultDto.setLastName("Unavailable");
        return defaultDto;
    }

}