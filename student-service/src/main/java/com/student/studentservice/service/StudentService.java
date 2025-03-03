package com.student.studentservice.service;

import com.student.studentservice.model.Student;
import com.student.studentservice.repository.StudentRepository;
import com.student.studentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StudentService {

    public StudentService(RestTemplate restTemplate, StudentRepository studentRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.restTemplate = restTemplate;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Student registerStudent(Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    public String loginStudent(String username, String password) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (passwordEncoder.matches(password, student.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public boolean existsByUsername(String username) {
        return studentRepository.findByUsername(username).isPresent();
    }

    public boolean verifyStudentExists(String studentId) {
        try {
            // Call student service using Eureka service name
            ResponseEntity<Boolean> response = restTemplate.getForEntity(
                    "http://student-service/students/verify/" + studentId,
                    Boolean.class);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Handle exceptions
            return false;
        }
    }
}