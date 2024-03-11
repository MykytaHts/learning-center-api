CREATE TABLE IF NOT EXISTS management.students_courses
(
    student_id         INTEGER,
    course_id          INTEGER,
    constraint pk_students_courses primary key (student_id, course_id),
    constraint fk_students_courses_student_id foreign key (student_id) references management.users (id),
    constraint fk_students_courses_course_id foreign key (course_id) references management.courses (id)
);

