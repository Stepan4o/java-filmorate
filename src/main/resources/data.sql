-- UPLOAD MPA
insert into mpa (mpa_name)
select 'G'
where not exists (
select * from mpa where mpa_name = 'G'
);

insert into mpa (mpa_name)
select 'PG'
where not exists (
select * from mpa where mpa_name = 'PG'
);

insert into mpa (mpa_name)
select 'PG-13'
where not exists (
select * from mpa where mpa_name = 'PG-13'
);


insert into mpa (mpa_name)
select 'R'
where not exists (
select * from mpa where mpa_name = 'R'
);


insert into mpa (mpa_name)
select 'NC-17'
where not exists (
select * from mpa where mpa_name = 'NC-17'
);

-- UPLOAD GENRE
insert into genre (genre_name)
select 'Комедия'
where not exists (
select genre_name from genre where genre_name = 'Комедия'
);

insert into genre (genre_name)
select 'Драма'
where not exists (
select genre_name from genre where genre_name = 'Драма'
);

insert into genre (genre_name)
select 'Мультфильм'
where not exists (
select genre_name from genre where genre_name = 'Мультфильм'
);

insert into genre (genre_name)
select 'Триллер'
where not exists (
select genre_name from genre where genre_name = 'Триллер'
);

insert into genre (genre_name)
select 'Документальный'
where not exists (
select genre_name from genre where genre_name = 'Документальный'
);

insert into genre (genre_name)
select 'Боевик'
where not exists (
select genre_name from genre where genre_name = 'Боевик'
);