CREATE TABLE IF NOT EXISTS management.questions
(
    id              SERIAL constraint pk_questions primary key,
    description     VARCHAR(255),
    order_index     INTEGER      NOT NULL,
    test_id         INTEGER      NOT NULL,
    question_complexity INTEGER      NOT NULL,
    question_type   INTEGER      NOT NULL,
    created_date    timestamp    NOT NULL,
    modified_date   timestamp    NOT NULL,
    created_by      VARCHAR(50)  NOT NULL,
    modified_by     VARCHAR(50)  NOT NULL,
    constraint fk_questions_test_id foreign key (test_id) references management.tests (id)
);

