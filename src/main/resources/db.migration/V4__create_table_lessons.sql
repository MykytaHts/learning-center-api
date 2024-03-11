CREATE TABLE IF NOT EXISTS management.lessons
(
    id     SERIAL constraint pk_lessons primary key,
    title         VARCHAR(255) NOT NULL,
    content       TEXT         NOT NULL,
    order_index   INTEGER,
    course_id     INTEGER,
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL,
    constraint fk_lessons_course_id foreign key (course_id) references management.courses (id)
);
