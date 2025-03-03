package com.student.detailsservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/details")
public class DetailsController {

    @GetMapping("/{id}")
    public String getStudentDetails(@PathVariable Long id) {
        // Fetch details from the database or another service
        return "Details for student with ID: " + id;
    }
}
