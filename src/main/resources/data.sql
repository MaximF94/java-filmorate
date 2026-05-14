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
