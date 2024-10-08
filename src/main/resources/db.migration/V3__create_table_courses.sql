CREATE TABLE IF NOT EXISTS management.courses
(
    id            SERIAL constraint pk_courses primary key,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    available     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_date  timestamp    NOT NULL,
    modified_date timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    modified_by   VARCHAR(50)  NOT NULL,
    constraint uq_courses_title unique (title)
);