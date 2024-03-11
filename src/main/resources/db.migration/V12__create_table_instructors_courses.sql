CREATE TABLE IF NOT EXISTS management.instructors_courses
(
    instructor_id         INTEGER,
    course_id             INTEGER,
    constraint pk_instructors_courses primary key (instructor_id, course_id),
    constraint fk_instructors_courses_instructor_id foreign key (instructor_id) references management.users (id),
    constraint fk_instructors_courses_course_id foreign key (course_id) references management.courses (id)
);

