package com.student.studentservice.controller;

import com.student.studentservice.client.StudentDetailsDto;
import com.student.studentservice.model.Student;
import com.student.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(studentService.registerStudent(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestParam String username, @RequestParam String password) {
        LoginResponse response = studentService.loginStudent(username, password);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{username}")
    public ResponseEntity<Boolean> verifyStudentExists(@PathVariable String username) {
        boolean exists = studentService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }

    // Inner class for registration request
    public static class RegistrationRequest {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;

        public RegistrationRequest(String username, String password, String firstName, String lastName, String email) {
            this.username = username;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        // Getters and setters

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // Inner class for login response
    public static class LoginResponse {
        private String token;
        private StudentDetailsDto details;

        public LoginResponse(String token, StudentDetailsDto details) {
            this.token = token;
            this.details = details;
        }

        public LoginResponse() {}

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public StudentDetailsDto getDetails() {
            return details;
        }

        public void setDetails(StudentDetailsDto details) {
            this.details = details;
        }

        // Constructor, getters and setters
    }
}