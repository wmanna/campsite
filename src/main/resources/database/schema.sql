CREATE SCHEMA IF NOT EXISTS campsite;

CREATE TABLE IF NOT EXISTS campsite.reservations (
    id varchar not null,
    code varchar not null unique,
    reservation_date timestamp not null,
    arrival_date timestamp not null,
    departure_date timestamp not null,
    cancellation_date timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS campsite.resource_locks (
    id varchar not null,
    locked boolean not null,
    lock_timestamp timestamp not null,
    PRIMARY KEY (id)
);

TRUNCATE TABLE campsite.resource_locks;

INSERT INTO campsite.resource_locks(id, locked, lock_timestamp) VALUES('RESERVATION_LOCK', false, now())
