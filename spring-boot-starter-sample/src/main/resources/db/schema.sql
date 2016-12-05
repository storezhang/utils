DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20),
    username VARCHAR(255),
    password VARCHAR(255)
);
CREATE UNIQUE INDEX username ON user (username);