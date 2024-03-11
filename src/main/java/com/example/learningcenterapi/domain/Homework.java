package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@ToString(of = {"student", "lesson", "filePath"})
@EqualsAndHashCode(callSuper = true, of = {"student", "lesson", "filePath"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "homeworks", schema = "management")
@Entity
public class Homework extends BaseEntity {
    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "student_id")
    @ManyToOne(optional = false)
    private Student student;

    @NotNull
    @JoinColumn(name = "lesson_id")
    @ManyToOne(optional = false)
    private Lesson lesson;

    @NotBlank
    @Size(min = 10, max = 255)
    @Column(name = "file_path")
    private String filePath;
}
