-- Add users
INSERT INTO users (id, username, password)
VALUES (1, 'test_admin', 'test_admin_password'),
       (2, 'test_user', 'test_user_password');

INSERT INTO user_role (user_id, roles)
VALUES (1, 'ADMIN'),
       (2, 'USER');

-- Add wines
INSERT INTO wines (id, country, name, price, type, url, volume, user_id)
VALUES (1, 'Espanja', 'Valkoviini 1', 8.75, 'WHITE', 'invalid', 0.75, 1),
       (2, 'Espanja', 'Valkoviini 2', 8.75, 'WHITE', 'invalid', 0.75, 1),
       (3, 'Ranska', 'Punaviini 1', 29.95, 'RED', 'invalid', 3.0, 1),
       (4, 'Italia', 'Punaviini 2', 30.95, 'RED', 'invalid', 3.0, 1);

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
       (3, 'tamminen');

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
INSERT INTO reviews (id, author, date, rating, review_text, user_id, wine_id)
VALUES (1, 'Mika', '2019-11-14', 3.0, 'Mikan uusi arvostelu', 1, 1),
       (2, 'Salla', '2019-11-15', 4.5, 'Sallan uusi arvostelu', 1, 1);
