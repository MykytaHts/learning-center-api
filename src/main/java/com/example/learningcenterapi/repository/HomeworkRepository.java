package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long>, JpaSpecificationExecutor<Homework> {
    List<Homework> findByStudentId(Long studentId);

    @Query("select h from Homework h join fetch h.lesson l " +
            "WHERE h.student.id = :userId and l.course.id= :courseId")
    List<Homework> findByStudentIdAndCourseId(Long userId, Long courseId);

    boolean existsByIdAndStudentId(Long homeworkId, Long id);
}
