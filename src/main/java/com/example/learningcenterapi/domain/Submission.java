package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "submissions", schema = "management")
@Entity
public class Submission extends BaseEntity {
    @EmbeddedId
    private SubmissionId id = new SubmissionId();

    @NotNull
    @MapsId(value = "studentId")
    @JoinColumn(name = "student_id")
    @ManyToOne(optional = false)
    private Student student;

    @NotNull
    @MapsId(value = "lessonId")
    @JoinColumn(name = "lesson_id")
    @ManyToOne(optional = false)
    private Lesson lesson;

    @NotNull
    private Double grade;

    public Submission(Student student, Lesson lesson, Double grade) {
        this.student = student;
        this.lesson = lesson;
        this.id = new SubmissionId(student.getId(), lesson.getId());
        this.grade = grade;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class SubmissionId implements Serializable {
        public Long studentId;
        public Long lessonId;
    }
}
