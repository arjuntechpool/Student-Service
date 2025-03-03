package com.student.studentservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StudentDetailsClient {

    @Autowired
    private RestTemplate restTemplate;

    // Used when registering a student to create a default profile
    public void createStudentDetails(String username, String token, String firstName, String lastName, String email) {
        String url = "http://student-details-service/details";

        // Create headers with the JWT token and X-User header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("X-User", username);

        // Create student details object
        StudentDetailsDto detailsDto = new StudentDetailsDto();
        detailsDto.setStudentId(username);
        detailsDto.setFirstName(firstName);
        detailsDto.setLastName(lastName);
        detailsDto.setEmail(email);

        HttpEntity<StudentDetailsDto> entity = new HttpEntity<>(detailsDto, headers);

        // Make the request
        restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);
    }

    // Used during login to fetch student details
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
}