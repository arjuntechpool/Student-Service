package com.student.studentservice.service;

import com.student.studentservice.client.StudentDetailsClient;
import com.student.studentservice.client.StudentDetailsDto;
import com.student.studentservice.controller.StudentController.LoginResponse;
import com.student.studentservice.controller.StudentController.RegistrationRequest;
import com.student.studentservice.model.Student;
import com.student.studentservice.repository.StudentRepository;
import com.student.studentservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentDetailsClient studentDetailsClient;

    public Student registerStudent(RegistrationRequest request) {
        // Check if username already exists
        if (studentRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create and save student
        Student student = new Student();
        student.setUsername(request.getUsername());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        Student savedStudent = studentRepository.save(student);

        // Generate token for the new student
        String token = jwtUtil.generateToken(request.getUsername());

        // Create student details through the client
        studentDetailsClient.createStudentDetails(
                request.getUsername(),
                token,
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );

        return savedStudent;
    }

    public LoginResponse loginStudent(String username, String password) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (passwordEncoder.matches(password, student.getPassword())) {
            // Generate token
            String token = jwtUtil.generateToken(username);

            // Fetch student details
            StudentDetailsDto details = studentDetailsClient.getStudentDetails(username, token);

            // Create response
            LoginResponse response = new LoginResponse();
            response.setToken(token);
            response.setDetails(details);

            return response;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public boolean existsByUsername(String username) {
        return studentRepository.findByUsername(username).isPresent();
    }
}