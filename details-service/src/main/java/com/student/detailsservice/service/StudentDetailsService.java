package com.student.detailsservice.service;

import com.student.detailsservice.model.StudentDetails;
import com.student.detailsservice.repository.StudentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentDetailsService {

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    public StudentDetails createStudentDetails(StudentDetails studentDetails) {
        return studentDetailsRepository.save(studentDetails);
    }

    public StudentDetails updateStudentDetails(String studentId, StudentDetails updatedDetails) {
        Optional<StudentDetails> existingDetails = studentDetailsRepository.findByStudentId(studentId);

        if (existingDetails.isPresent()) {
            StudentDetails details = existingDetails.get();
            details.setFirstName(updatedDetails.getFirstName());
            details.setLastName(updatedDetails.getLastName());
            details.setEmail(updatedDetails.getEmail());
            details.setPhoneNumber(updatedDetails.getPhoneNumber());
            details.setAddress(updatedDetails.getAddress());
            details.setDateOfBirth(updatedDetails.getDateOfBirth());
            details.setDepartment(updatedDetails.getDepartment());
            details.setEnrollmentYear(updatedDetails.getEnrollmentYear());

            return studentDetailsRepository.save(details);
        } else {
            throw new RuntimeException("Student details not found for studentId: " + studentId);
        }
    }

    public StudentDetails getStudentDetails(String studentId) {
        return studentDetailsRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student details not found for studentId: " + studentId));
    }

    public List<StudentDetails> getAllStudentDetails() {
        return studentDetailsRepository.findAll();
    }

    public void deleteStudentDetails(String studentId) {
        StudentDetails details = studentDetailsRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student details not found for studentId: " + studentId));
        studentDetailsRepository.delete(details);
    }
}