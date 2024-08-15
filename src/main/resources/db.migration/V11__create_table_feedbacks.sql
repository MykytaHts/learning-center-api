CREATE TABLE IF NOT EXISTS management.feedbacks
(
    id SERIAL constraint pk_feedbacks primary key,
    student_id         INTEGER,
    course_id          INTEGER,
    feedback           VARCHAR(255) NOT NULL,
    constraint fk_feedbacks_student_id foreign key (student_id) references management.users (id),
    constraint fk_feedbacks_course_id foreign key (course_id) references management.courses (id)
);
