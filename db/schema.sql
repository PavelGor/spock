create database spock;

create sequence users_id_seq
	as integer
	maxvalue 2147483647
;

create sequence lots_id_seq
	as integer
	maxvalue 2147483647
;

create sequence bets_id_seq
	as integer
	maxvalue 2147483647
;

create sequence messages_id_seq
	as integer
	maxvalue 2147483647
;

create table lots
(
	id serial not null
		constraint lots_pkey
			primary key,
	name varchar not null,
	description varchar,
	start_price real not null,
	current_price real,
	start_time timestamp,
	end_time timestamp,
	picture_link varchar,
	status varchar(1) not null default 'W'
)
;

create unique index lots_id_uindex
	on lots (id)
;

create table users
(
	id serial not null
		constraint users_pkey
			primary key,
	login varchar not null,
	password varchar not null,
	salt varchar not null
)
;

create unique index users_id_uindex
	on users (id)
;

create unique index users_login_uindex
	on users (login)
;

create table bets
(
	id serial not null
		constraint bets_pkey
			primary key
		constraint lot_id
			references lots (id)
		constraint user_id
			references users,
	price real not null,
	time timestamp not null
)
;

create table messages
(
	id serial not null
		constraint messages_pkey
			primary key
		constraint user_id
			references users,
	type varchar not null,
	message_text varchar not null,
	viewed boolean not null
)
;

create unique index messages_id_uindex
	on messages (id)
;

