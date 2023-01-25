create table events
(
    id bigint not null auto_increment,
    active bit,
    description varchar(255),
    endDate datetime,
    startDate datetime,
    title varchar(255),
    eventsByType bigint,
    primary key (id)
);

create table events_like
(
    user_id bigint not null,
    event_id bigint not null,
    primary key (user_id, event_id)
);

create table events_type
(
    id bigint not null auto_increment,
    type varchar(255), primary key (id)
);

create table roles
(
    id bigint not null auto_increment,
    roleTitle varchar(255),
    primary key (id)
);

create table users
(
    id bigint not null auto_increment,
    active bit,
    login varchar(255) not null,
    password varchar(255) not null,
    role_id bigint, primary key (id)
);

create table users_info
(
    id bigint not null,
    email varchar(255),
    name varchar(255),
    phone varchar(255),
    surname varchar(255),
    primary key (id)
);

alter table events
    add foreign key (eventsByType) references events_type (id);

alter table events_like
    add foreign key (event_id) references events (id);
alter table events_like
    add foreign key (user_id) references users (id);

alter table users
    add foreign key (role_id) references roles (id);

alter table users_info
    add foreign key (id) references users (id);