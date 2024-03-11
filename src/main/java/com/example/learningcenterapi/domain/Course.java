package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true, exclude = {"instructors", "students", "lessons"})
@ToString(callSuper = true, exclude = {"instructors", "students", "lessons"})
@EntityListeners(AuditingEntityListener.class)
@Table(name = "courses", schema = "management")
@Entity
public class Course extends BaseEntity {
    @Serial
    private final static long serialVersionUID = 1L;
    private static final int MIN_LESSONS_REQUIRED = 5;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 255)
    @Column(unique = true)
    private String title;

    @NotBlank
    @Size(min = 2, max = 255)
    @Lob
    private String description;

    @OneToMany(mappedBy = "course",
            cascade = {PERSIST, MERGE},
            orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    @Setter(value = PRIVATE)
    @OneToMany(mappedBy = "course",
            cascade = {PERSIST, MERGE},
            orphanRemoval = true)
    private Set<Test> tests = new HashSet<>();

    @ManyToMany(mappedBy = "courses",
            cascade = {PERSIST, MERGE})
    private Set<Instructor> instructors = new HashSet<>();

    @Setter(value = PRIVATE)
    @ManyToMany(mappedBy = "courses",
            cascade = {PERSIST, MERGE})
    private Set<Student> students = new HashSet<>();

    private boolean available;

    @PreRemove
    public void removeAssociations() {
        students.forEach(this::removeStudent);
        students.clear();
        instructors.forEach(this::removeInstructor);
        instructors.clear();
    }

    public void setInstructors(Set<Instructor> instructors) {
        this.instructors.clear();
        addAllInstructors(instructors);
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons.clear();
        addAllLessons(lessons);
    }

    public void addAllInstructors(Collection<Instructor> instructors) {
        checkEmpty(instructors, "Instructors collection");
        instructors.forEach(this::addInstructor);
    }

    public void addAllLessons(Collection<Lesson> lessons) {
        checkEmpty(lessons, "Lessons collection");
        lessons.forEach(this::addLesson);
    }

    public void addInstructor(Instructor instructor) {
        checkNull(instructor, "Instructor");
        this.instructors.add(instructor);
        instructor.getCourses().add(this);
        setAvailable();
    }

    public void addLesson(Lesson lesson) {
        checkNull(lesson, "Lesson");
        this.lessons.add(lesson);
        lesson.setCourse(this);
        setAvailable();
    }

    public void removeInstructor(Instructor instructor) {
        checkNull(instructor, "Instructor");
        instructors.remove(instructor);
        instructor.getCourses().remove(this);
        setAvailable();
    }

    private void removeStudent(Student student) {
        checkNull(student, "Instructor");
        students.remove(student);
        student.getCourses().remove(this);
    }

    public void removeLesson(Lesson lesson) {
        checkNull(lesson, "Lesson");
        checkIfContainsLesson(lesson);
        lessons.remove(lesson);
        lesson.setCourse(null);
        setAvailable();
    }

    private void setAvailable() {
        this.available = CollectionUtils.isNotEmpty(instructors) && lessons.size() >= MIN_LESSONS_REQUIRED;
    }

    private void checkAvailable() {
        if (!available) {
            throw new IllegalStateException("Course is not available for more lessons");
        }
    }

    private void checkIfContainsLesson(Lesson lesson) {
        if (!this.lessons.contains(lesson)) {
            throw new IllegalStateException("Lesson " + lesson + " is not a part of the course with id " + id + ".");
        }
    }
}
