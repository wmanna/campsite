CREATE SCHEMA IF NOT EXISTS campsite;

CREATE TABLE IF NOT EXISTS campsite.users (
    id varchar not null,
    email varchar not null unique,
    full_name varchar not null,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS campsite.reservations (
    id varchar not null,
    code varchar not null unique,
    reservation_date timestamp not null,
    arrival_date timestamp not null,
    departure_date timestamp not null,
    cancellation_date timestamp,
    user_id varchar not null,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS campsite.resource_locks (
    id varchar not null,
    locked boolean not null,
    lock_timestamp timestamp not null,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS campsite.reservation_calendar (
    id varchar not null,
    calendar_date date not null unique,
    reservation_code varchar,
    cancelled boolean,
    PRIMARY KEY (id),
    FOREIGN KEY (reservation_code) REFERENCES reservations(code)
);

TRUNCATE TABLE campsite.resource_locks;

INSERT INTO campsite.resource_locks(id, locked, lock_timestamp) VALUES('RESERVATION_LOCK', false, now())
