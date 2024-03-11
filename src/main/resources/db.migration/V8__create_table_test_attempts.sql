CREATE TABLE IF NOT EXISTS management.test_attempts
(
    test_id       INTEGER,
    student_id    INTEGER,
    grade         double precision NOT NULL,
    status        VARCHAR(50) NOT NULL,
    created_date  timestamp   NOT NULL,
    created_by    VARCHAR(50) NOT NULL,
    constraint pk_test_attempts primary key (test_id, student_id),
    constraint fk_test_attempts_lesson_id foreign key (test_id) references management.tests (id),
    constraint fk_test_attempts_test_id foreign key (student_id) references management.users (id)
);
