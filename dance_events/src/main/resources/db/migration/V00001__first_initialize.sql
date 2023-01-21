-- ensure that the table is removed before creating a new one.
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS users_info;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS events_type;
DROP TABLE IF EXISTS events_like;
DROP TABLE IF EXISTS roles;

CREATE TABLE users
(
    id   BIGINT AUTO_INCREMENT NOT NULL ,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BIT,
    role_id BIGINT REFERENCES roles(id),
    PRIMARY KEY (id)
);

CREATE TABLE users_info
(
    id BIGINT NOT NULL,
    name VARCHAR(255),
    surname VARCHAR(255),
    phone VARCHAR(255),
    email VARCHAR(255),
    FOREIGN KEY (id) REFERENCES users(id),
    PRIMARY KEY (id)
);

CREATE TABLE events
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    title VARCHAR(255),
    startDate DATETIME,
    endDate DATETIME,
    description VARCHAR(255),
    active BIT,
    eventsByType BIGINT REFERENCES events_type(id),
    PRIMARY KEY (id)
);

CREATE TABLE events_type
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    type VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE events_like
(
    user_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (event_id) REFERENCES events(id),
    UNIQUE (user_id, event_id)
);

CREATE TABLE roles
(
    id BIGINT AUTO_INCREMENT NOT NULL,
    roleTitle VARCHAR(255),
    PRIMARY KEY (id)
);