package com.student.detailsservice.controller;

import com.student.detailsservice.model.StudentDetails;
import com.student.detailsservice.service.StudentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/details")
public class StudentDetailsController {

    @Autowired
    private StudentDetailsService studentDetailsService;

    @PostMapping
    public ResponseEntity<StudentDetails> createStudentDetails(@RequestBody StudentDetails studentDetails,
                                                               @RequestHeader("X-User") String username) {
        // Set the studentId from the authenticated user
        studentDetails.setStudentId(username);
        return new ResponseEntity<>(studentDetailsService.createStudentDetails(studentDetails), HttpStatus.CREATED);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDetails> getStudentDetails(@PathVariable String studentId) {
        return ResponseEntity.ok(studentDetailsService.getStudentDetails(studentId));
    }

    @GetMapping
    public ResponseEntity<List<StudentDetails>> getAllStudentDetails() {
        return ResponseEntity.ok(studentDetailsService.getAllStudentDetails());
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDetails> updateStudentDetails(
            @PathVariable String studentId,
            @RequestBody StudentDetails studentDetails,
            @RequestHeader("X-User") String username) {
        // Check if the authenticated user matches the requested studentId
        if (!username.equals(studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(studentDetailsService.updateStudentDetails(studentId, studentDetails));
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudentDetails(@PathVariable String studentId,
                                                     @RequestHeader("X-User") String username) {
        // Check if the authenticated user matches the requested studentId
        if (!username.equals(studentId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        studentDetailsService.deleteStudentDetails(studentId);
        return ResponseEntity.noContent().build();
    }
}