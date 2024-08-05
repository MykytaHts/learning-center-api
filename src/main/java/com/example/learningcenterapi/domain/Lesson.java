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
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;


@Getter
@Setter
@ToString(of = {"title", "content"})
@EqualsAndHashCode(callSuper = true, of = {"title", "content"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "lessons", schema = "management")
@Entity
public class Lesson extends BaseEntity {
    @Serial
    private final static long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Size(min = 2, max = 255)
    @NotBlank
    private String title;

    @NotBlank
    @Lob
    private String content;

    @NotNull
    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToOne(mappedBy = "lesson",
            fetch = LAZY,
            cascade = REMOVE,
            orphanRemoval = true)
    private Test test;

    @JoinColumn(name = "course_id")
    @ManyToOne(fetch = LAZY, optional = false)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = REMOVE)
    private List<Submission> submissions;

    public void setTest(Test test) {
        this.test = test;
        test.setLesson(this);
    }
}
