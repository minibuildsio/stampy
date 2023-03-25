create extension pgcrypto;

create table stampyuser (
  id serial primary key,
  username character varying(128) not null unique,
  password_hash character varying(64) not null
);