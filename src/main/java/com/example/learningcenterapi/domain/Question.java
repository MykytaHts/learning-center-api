package com.example.learningcenterapi.domain;

import com.example.learningcenterapi.domain.enumeration.QuestionComplexity;
import com.example.learningcenterapi.domain.enumeration.QuestionType;
import com.example.learningcenterapi.util.convertor.QuestionComplexityConvertor;
import com.example.learningcenterapi.util.convertor.QuestionTypeConvertor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.example.learningcenterapi.util.SystemValidator.checkEmpty;
import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@ToString(callSuper = true, exclude = "options")
@EqualsAndHashCode(callSuper = true, exclude = "options")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "questions", schema = "management")
@Entity
public class Question extends BaseEntity {
    @Serial
    private final static long serialVersionUID = 1L;
    private final static int MAX_OPTIONS = 6;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(min = 5, max = 255)
    @NotBlank
    private String description;

    @NotNull
    @Column(name = "order_index")
    private Integer orderIndex;

    @NotNull
    @Convert(converter = QuestionTypeConvertor.class)
    @Column(name = "question_type")
    private QuestionType questionType;

    @NotNull
    @Convert(converter = QuestionComplexityConvertor.class)
    @Column(name = "question_complexity")
    private QuestionComplexity questionComplexity;

    @NotNull
    @JoinColumn(name = "test_id")
    @ManyToOne(optional = false)
    private Test test;

    @OneToMany(mappedBy = "question",
            cascade = {PERSIST, MERGE, REMOVE},
            orphanRemoval = true)
    private Set<Option> options = new HashSet<>();

    public void setOptions(Set<Option> options) {
        this.options.clear();
        addAllOptions(options);
    }

    public void addOption(Option option) {
        checkNull(option, "Option");
        checkQuestionIsFull();
        options.add(option);
        option.setQuestion(this);
    }

    public void addAllOptions(Collection<Option> options) {
        checkEmpty(options, "Options collection");
        checkQuestionIsFull(options);
        options.forEach(this::addOption);
    }

    public void removeOption(Option option) {
        checkNull(option, "Option");
        checkIfContains(option);
        this.options.remove(option);
        option.setQuestion(null);
    }

    private void checkIfContains(Option option) {
        if (this.options.contains(option)) {
            throw new IllegalStateException("Option " + option + " is already part of the question with id " + id + ".");
        }
    }

    private void checkQuestionIsFull(Collection<Option> options) {
        if (this.options.size() + options.size() > MAX_OPTIONS) {
            int availableSpace = MAX_OPTIONS - this.options.size();
            throw new IllegalArgumentException("Too many options provided. Available space: " + availableSpace + ".");
        }
    }

    private void checkQuestionIsFull() {
        if (CollectionUtils.size(this.options) >= MAX_OPTIONS) {
            throw new IllegalStateException("Test is already full. You can't add more options.");
        }
    }
}
