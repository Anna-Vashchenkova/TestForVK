drop table if exists users cascade;
create table if not exists users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name varchar(50) not null,
    email varchar(25) not null,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
    );
