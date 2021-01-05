CREATE SCHEMA IF NOT EXISTS campsite;

CREATE TABLE IF NOT EXISTS campsite.reservations (
    id varchar not null,
    code varchar not null,
    reservation_date timestamp not null,
    PRIMARY KEY (id)
);