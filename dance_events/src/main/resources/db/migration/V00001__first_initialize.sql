-- ensure that the table is removed before creating a new one.
DROP TABLE IF EXISTS users;

-- Create user table
CREATE TABLE users
(
    id   BIGINT AUTO_INCREMENT,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN,
    PRIMARY KEY (id)
);