package com.example.learningcenterapi.domain;

import com.example.learningcenterapi.domain.enumeration.TestCompletionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"id", "status", "grade"})
@EqualsAndHashCode(of = {"status", "grade"})
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "test_attempts", schema = "management")
@Entity
public class TestAttempt implements Serializable{
    @Serial
    private final static long serialVersionUID = 1L;

    @EmbeddedId
    private TestAttemptId id;

    @NotNull
    private TestCompletionStatus status;

    @NotNull
    @DecimalMin(value = "0.0")
    private Double grade;

    @NotNull
    @MapsId(value = "studentId")
    @JoinColumn(name = "student_id")
    @ManyToOne(optional = false)
    private Student student;

    @NotNull
    @MapsId(value = "testId")
    @JoinColumn(name = "test_id")
    @ManyToOne(optional = false)
    private Test test;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    public TestAttempt(Student student, Test test, Double grade, TestCompletionStatus status) {
        this.student = student;
        this.test = test;
        this.id = new TestAttemptId(student.getId(), test.getId());
        this.grade = grade;
        this.status = status;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class TestAttemptId implements Serializable {
        public Long studentId;
        public Long testId;
    }
}
