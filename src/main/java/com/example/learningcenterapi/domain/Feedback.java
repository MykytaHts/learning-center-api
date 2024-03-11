package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Setter
@Getter
@ToString(of = {"feedback"})
@EqualsAndHashCode(of = {"feedback"})
@Table(name = "feedbacks", schema = "management")
@Entity
public class Feedback implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @JoinColumn(name = "student_id")
    @ManyToOne(optional = false, fetch = LAZY)
    private Student student;

    @NotNull
    @JoinColumn(name = "course_id")
    @ManyToOne(optional = false, fetch = LAZY)
    private Course course;

    @NotBlank
    @Size(min = 10, max = 255)
    private String feedback;
}
