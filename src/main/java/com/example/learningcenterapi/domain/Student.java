package com.example.learningcenterapi.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

import static com.example.learningcenterapi.util.SystemValidator.checkNull;
import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.PRIVATE;

@Setter
@Getter
@DiscriminatorValue("STUDENT")
@EqualsAndHashCode(callSuper = true, exclude = "courses")
@ToString(callSuper = true, exclude = "courses")
@Entity
public class Student extends User {
    @Serial
    private final static long serialVersionUID = 1L;
    private final static int MAX_COURSES = 5;

    @Setter(value = PRIVATE)
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(schema = "management",
                name = "students_courses",
                joinColumns = @JoinColumn(name = "student_id"),
                inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        checkNull(course, "Course");
        checkCourseCapacity();
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }

    private void checkCourseCapacity() {
        if (CollectionUtils.size(this.courses) >= MAX_COURSES) {
            throw new IllegalStateException("User has been reached the limit on courses.");
        }
    }
}
