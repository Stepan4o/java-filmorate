CREATE TABLE IF NOT EXISTS USERS (
 user_id int PRIMARY KEY AUTO_INCREMENT,
 email varchar(255) not null unique,
 login varchar(255), USER_NAME varchar(255),
 birthday date
);

CREATE TABLE IF NOT EXISTS FILMS (
 film_id int PRIMARY KEY AUTO_INCREMENT,
 film_name varchar(255) not null,
 description varchar(200) not null,
 release_date date not null,
 duration int not null,
 mpa_id_fk int not null
);

CREATE TABLE IF NOT EXISTS GENRE (
 genre_id int PRIMARY KEY AUTO_INCREMENT,
 genre_name varchar(255) not null
);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
 film_id_fk int not null references FILMS(FILM_ID),
 genre_id_fk int not null references GENRE(GENRE_ID)
);

CREATE TABLE IF NOT EXISTS MPA (
 mpa_id int PRIMARY KEY AUTO_INCREMENT,
 mpa_name varchar(255) not null
);

ALTER TABLE FILMS ADD FOREIGN KEY (MPA_ID_FK) REFERENCES MPA(MPA_ID);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
 user_id_fk int references USERS(USER_ID),
 friend_id_fk int references USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS LIKES (
 user_id_fk int not null references USERS(USER_ID),
 film_id_fk int not null references FILMS(FILM_ID)
);