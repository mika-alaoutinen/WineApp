-- Hibernate sequence for generating IDs.
-- Start at 9 because data.sql persists 8 entities
-- and I want to set their IDs explicitly.
CREATE SEQUENCE hibernate_sequence START 9 INCREMENT 1;

-- Users
CREATE TABLE IF NOT EXISTS users
(
    id       serial primary key,
    password varchar not null,
    username varchar not null
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id bigint primary key,
    roles   varchar
);

-- Wines
CREATE TABLE IF NOT EXISTS wines
(
    id      serial primary key,
    country varchar          not null,
    name    varchar          not null,
    price   double precision not null,
    type    varchar(10)      not null,
    url     varchar,
    volume  double precision not null,
    user_id bigint           not null
);

CREATE TABLE IF NOT EXISTS wine_descriptions
(
    id          bigint,
    description varchar
);

CREATE TABLE IF NOT EXISTS wine_food_pairings
(
    id            bigint,
    food_pairings varchar
);

-- Reviews
CREATE TABLE IF NOT EXISTS reviews
(
    id          serial primary key,
    author      varchar          not null,
    date        date             not null,
    rating      double precision not null,
    review_text text             not null,
    user_id     bigint           not null,
    wine_id     bigint           not null
);
