CREATE TABLE tasks
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL UNIQUE,
    description  VARCHAR(255),
    user_id      VARCHAR(255) NOT NULL,
    status_id    INT NOT NULL,
    created_at   TIMESTAMP,
    completed_at TIMESTAMP,
    FOREIGN KEY (status_id) REFERENCES statuses (id)
);