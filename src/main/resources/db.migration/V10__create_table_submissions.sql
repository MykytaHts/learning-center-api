CREATE TABLE IF NOT EXISTS management.submissions
(
    lesson_id     INTEGER,
    student_id    INTEGER,
    grade         double precision,
    created_date  timestamp   NOT NULL,
    modified_date timestamp   NOT NULL,
    created_by    VARCHAR(50) NOT NULL,
    modified_by   VARCHAR(50) NOT NULL,
    constraint pk_submissions primary key (lesson_id, student_id),
    constraint fk_submissions_lesson_id foreign key (lesson_id) references management.lessons (id),
    constraint fk_submissions_student_id foreign key (student_id) references management.users (id)
);