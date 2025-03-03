package com.student.detailsservice.repository;

import com.student.detailsservice.model.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDetailsRepository extends JpaRepository<StudentDetails, Long> {
    Optional<StudentDetails> findByStudentId(String studentId);
}