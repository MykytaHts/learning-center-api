package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {
    List<Feedback> findAllByCourseId(Long courseId);
    boolean existsByIdAndStudentId(Long id, Long studentId);
}
