MERGE INTO PUBLIC.MPA_RATINGS (MPA_RATING_ID, NAME)
KEY (MPA_RATING_ID)
VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

MERGE INTO PUBLIC.GENRES (GENRE_ID, NAME)
KEY (GENRE_ID)
VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

MERGE INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASEDATE, DURATION, MPA_RATING_ID)
KEY (NAME, RELEASEDATE)
VALUES
    ('Матрица', 'Хакер узнаёт правду о реальности', '1999-03-31', 136, 4),
    ('Король Лев', 'Приключения львёнка Симбы', '1994-06-15', 88, 1),
    ('Начало', 'Вор внедряет идею в подсознание', '2010-07-16', 148, 3);

MERGE INTO PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
KEY (EMAIL)
VALUES
    ('alice@example.com', 'alice_wonder', 'Алиса', '1990-05-15'),
    ('bob@example.com', 'bob_marley', 'Боб', '1988-12-03'),
    ('charlie@example.com', 'charlie_brown', 'Чарли', '1995-07-22');



MERGE INTO PUBLIC.FRIENDS (USER_ID, FRIEND_ID)
KEY (USER_ID, FRIEND_ID)
VALUES
    (1, 3),
    (2, 1),
    (2, 3),
    (3, 1),
    (3, 2);


MERGE INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID)
KEY (FILM_ID, USER_ID)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 1);


MERGE INTO PUBLIC.FILMS_GENRES (FILM_ID, GENRE_ID)
KEY (FILM_ID, GENRE_ID)
VALUES
    (1, 3),
    (1, 4),
    (2, 5),
    (3, 4),
    (3, 6);
