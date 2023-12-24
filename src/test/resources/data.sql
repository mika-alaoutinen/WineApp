-- Add users
-- Passwords are encrypted with Bcrypt. Unencrypted passwords are
-- "test_admin_password" and "test_user_password"
INSERT INTO users (username, password)
VALUES ('test_admin', '$2a$10$eRdUjwSdDTDYZJJ3y2a8BeNWooOz41ToaAgo7m0b.eZXa5000h7bO'),
       ('test_user', '$2a$10$ffHMw9WlT1ao8uKiC3lHvuDcEQAOo7BzYzQohXQzc1wZn6C4Xl6/S');

INSERT INTO user_role (user_id, roles)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

-- Add wines
INSERT INTO wine (country, name, price, type, url, volume, user_id)
VALUES ('Espanja', 'Valkoviini 1', 8.75, 'WHITE', 'invalid', 0.75, 2),
       ('Chile', 'Valkoviini 2', 7.50, 'WHITE', 'invalid', 0.75, 2),
       ('Ranska', 'Punaviini 1', 29.95, 'RED', 'invalid', 3.0, 2),
       ('Italia', 'Punaviini 2', 30.95, 'RED', 'invalid', 3.0, 2);

INSERT INTO wine_descriptions (id, description)
VALUES (1, 'puolikuiva'),
       (1, 'sitruunainen'),
       (1, 'yrttinen'),
       (2, 'kuiva'),
       (2, 'sitruunainen'),
       (2, 'pirskahteleva'),
       (3, 'tanniininen'),
       (3, 'mokkainen'),
       (3, 'täyteläinen'),
       (4, 'tamminen');

INSERT INTO wine_food_pairings (id, food_pairings)
VALUES (1, 'kala'),
       (1, 'kasvisruoka'),
       (1, 'seurustelujuoma'),
       (2, 'kana'),
       (2, 'kasvisruoka'),
       (2, 'juustot'),
       (3, 'nauta'),
       (3, 'pataruuat'),
       (3, 'pasta');

-- Add reviews
INSERT INTO review (author, date, rating, review_text, user_id, wine_id)
VALUES ('Mika', '2019-11-14', 3.0, 'Mikan uusi arvostelu', 2, 1),
       ('Salla', '2019-11-15', 4.5, 'Sallan uusi arvostelu', 2, 1);
