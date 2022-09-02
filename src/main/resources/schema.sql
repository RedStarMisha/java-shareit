CREATE TABLE IF NOT EXISTS users (
    id bigint generated by default as identity primary key,
    name varchar(256),
    email varchar(512) unique
);

CREATE TABLE IF NOT EXISTS requests (
    id bigint generated by default as identity primary key ,
    description varchar(512),
    requestor bigint references users(id) on DELETE cascade ,
    created date
);

CREATE TABLE IF NOT EXISTS items (
    id bigint generated by default as identity primary key ,
    name varchar(256),
    description varchar(512),
    user_id bigint references users(id) on DELETE cascade ,
    available boolean,
    request_id bigint references requests(id) on delete cascade
);


CREATE TABLE IF NOT EXISTS bookings (
    id bigint generated by default as identity primary key ,
    start_date date,
    end_date date,
    item_id bigint references items(id),
    booker_id bigint references users(id),
    status int
);


-- insert into booking_status as bs (id, name) values (1, 'WAITING'), (2, 'APPROVED'), (3, 'REJECTED'), (4, 'CANCELED')
--                            on conflict (id) do nothing;



