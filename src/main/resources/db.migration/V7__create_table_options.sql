CREATE TABLE IF NOT EXISTS management.options
(
    id            SERIAL constraint pk_options primary key,
    question_id   INTEGER      NOT NULL,
    correct       BOOLEAN      NOT NULL DEFAULT FALSE,
    content       VARCHAR(255) NOT NULL,
    created_date  timestamp    NOT NULL,
    created_by    VARCHAR(50)  NOT NULL,
    constraint fk_options_question_id foreign key (question_id) references management.questions (id)
);

