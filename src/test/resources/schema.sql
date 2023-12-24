-- Users
CREATE TABLE IF NOT EXISTS users
(
    id              serial         primary key,
    password        varchar        not null,
    username        varchar        not null
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id         bigint          primary key,
    roles           varchar
);

-- Wines
CREATE TABLE IF NOT EXISTS wine
(
    id              serial           primary key,
    country         varchar          not null,
    name            varchar          not null,
    price           double           not null,
    type            varchar(10)      not null,
    url             varchar,
    volume          double           not null,
    user_id         bigint           not null
);

CREATE TABLE IF NOT EXISTS wine_descriptions
(
    id              bigint,
    description     varchar
);

CREATE TABLE IF NOT EXISTS wine_food_pairings
(
    id              bigint,
    food_pairings   varchar
);

-- Reviews
CREATE TABLE IF NOT EXISTS review
(
    id              serial           primary key,
    author          varchar          not null,
    date            date             not null,
    rating          double           not null,
    review_text     text             not null,
    user_id         bigint           not null,
    wine_id         bigint           not null
);
