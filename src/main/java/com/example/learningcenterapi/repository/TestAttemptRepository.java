package com.example.learningcenterapi.repository;

import com.example.learningcenterapi.domain.TestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long>, JpaSpecificationExecutor<TestAttempt> {
    Optional<TestAttempt> findById_TestIdAndId_StudentId(final Long testId, final Long studentId);
}
