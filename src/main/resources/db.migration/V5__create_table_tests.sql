CREATE TABLE IF NOT EXISTS management.tests
(
    id            SERIAL constraint pk_tests primary key,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    available     BOOLEAN      NOT NULL DEFAULT FALSE,
    lesson_id     INTEGER      NOT NULL,
    course_id     INTEGER      NOT NULL,
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL,
    constraint fk_tests_lesson_id foreign key (lesson_id) references management.lessons (id),
    constraint fk_tests_course_id foreign key (course_id) references management.courses (id)
);