package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.learningcenterapi.util.SystemValidator.checkEmpty;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString(callSuper = true, of = {"title", "description"})
@EqualsAndHashCode(callSuper = true, of = {"title", "description"})
@Table(name = "tests", schema = "management")
@Entity
public class Test extends BaseEntity {
    @Serial
    private final static long serialVersionUID = 1L;
    private final static int MAX_QUESTIONS = 10;
    private final static int MIN_QUESTIONS = 2;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(min = 2, max = 255)
    @NotBlank
    private String title;

    @Size(min = 10, max = 255)
    @NotBlank
    private String description;

    private boolean available;

    @NotNull
    @JoinColumn(name = "lesson_id")
    @OneToOne(optional = false)
    private Lesson lesson;

    @NotNull
    @JoinColumn(name = "course_id")
    @ManyToOne(optional = false)
    private Course course;

    @OneToMany(mappedBy = "test", cascade = {PERSIST, MERGE}, orphanRemoval = true)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "test", cascade = REMOVE)
    private List<TestAttempt> testAttempts;

    public void setQuestions(List<Question> questions) {
        this.questions.clear();
        addAllQuestions(questions);
    }

    public void addQuestion(Question question) {
        checkNull(question, "Question");
        checkTestIsFull();
        this.questions.add(question);
        question.setTest(this);
        setAvailable();
    }

    public void addAllQuestions(Collection<Question> questions) {
        checkEmpty(questions, "Questions collection");
        questions.forEach(this::addQuestion);
    }

    public void removeQuestion(Question question) {
        checkNull(question, "Question");
        checkIfContains(question);
        this.questions.remove(question);
        question.setTest(null);
        setAvailable();
    }

    private void setAvailable() {
        this.available = !CollectionUtils.sizeIsEmpty(this.questions) &&
                this.questions.size() <= MAX_QUESTIONS &&
                this.questions.size() >= MIN_QUESTIONS;
    }

    private void checkIfContains(Question question) {
        if (!this.questions.contains(question)) {
            throw new IllegalStateException("Question " + question + " is not a part of the test with id " + id + ".");
        }
    }

    private void checkTestIsFull() {
        if (CollectionUtils.size(this.questions) >= MAX_QUESTIONS) {
            throw new IllegalStateException("Test is already full. You can't add more questions.");
        }
    }
}
