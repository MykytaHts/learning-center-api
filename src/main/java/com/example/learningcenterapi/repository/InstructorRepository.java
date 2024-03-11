package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>, JpaSpecificationExecutor<Instructor> {
    List<Instructor> findAllByCoursesId(Long courseId);

    boolean existsByIdAndCoursesId(Long instructorId, Long courseId);
}
