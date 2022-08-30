CREATE TABLE IF NOT EXISTS users (
    id bigint generated by default as identity primary key,
    name varchar(256),
    email varchar(512) unique
);

CREATE TABLE if not exists requests (
    id bigint generated by default as identity primary key ,
    description varchar(512),
    requestor bigint references users(id),
    date date
);

CREATE TABLE IF NOT EXISTS items (
    id bigint generated by default as identity primary key ,
    name varchar(256),
    description varchar(512),
    user_id bigint references users(id) on DELETE cascade ,
    available boolean,
    request_id bigint references requests(id) on delete cascade
);

