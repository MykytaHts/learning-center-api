package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository
        extends JpaRepository<Submission, Submission.SubmissionId>, JpaSpecificationExecutor<Submission> {
    Optional<Submission> findById_LessonIdAndId_StudentId(Long lessonId, Long studentId);
    List<Submission> findAllByStudentId(Long studentId);
    List<Submission> findAllByLessonId(Long lessonId);

    List<Submission> findAllByStudentIdAndLessonCourseId(Long studentId, Long courseId);
    boolean existsById_LessonIdAndId_StudentId(Long lessonId, Long studentId);
}
