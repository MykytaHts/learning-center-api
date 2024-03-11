package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static lombok.AccessLevel.PRIVATE;


@Setter
@Getter
@DiscriminatorValue("INSTRUCTOR")
@EqualsAndHashCode(callSuper = true, exclude = "courses")
@ToString(callSuper = true, exclude = "courses")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Instructor extends User {
    @Serial
    private final static long serialVersionUID = 1L;

    @Setter(value = PRIVATE)
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(schema = "management",
                name = "instructors_courses",
                joinColumns = @JoinColumn(name = "instructor_id"),
                inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        checkNull(course, "Course");
        courses.add(course);
        course.getInstructors().add(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.getInstructors().remove(this);
    }

    @PreRemove
    public void removeAssociations() {
        for (Course course : courses) {
            course.getInstructors().remove(this);
        }
        courses.clear();
    }
}
