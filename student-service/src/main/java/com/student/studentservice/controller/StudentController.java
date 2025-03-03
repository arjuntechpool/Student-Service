package com.student.studentservice.controller;

import com.student.studentservice.model.Student;
import com.student.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public Student registerStudent(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }

    @PostMapping("/login")
    public String loginStudent(@RequestParam String username, @RequestParam String password) {
        return studentService.loginStudent(username, password);
    }
}
