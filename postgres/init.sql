CREATE TYPE room_type AS ENUM ('classroom', 'office', 'open_space', 'other');
CREATE TYPE user_type AS ENUM ('student', 'teacher', 'admin');
CREATE TYPE registration_type AS ENUM ('student', 'teacher');
CREATE TABLE IF NOT EXISTS floors (
    id serial primary key,
    campus_code varchar(3) not null,
    floor_number integer not null,
    name varchar(50) not null
);
CREATE TABLE IF NOT EXISTS campus (
    code varchar(3) not null primary key,
    name varchar(50) not null,
    main_floor serial not null,
    foreign key (main_floor) references floors(id)
);
CREATE TABLE IF NOT EXISTS rooms (
    id serial primary key,
    floor_id integer not null,
    campus_code varchar(3) not null,
    type room_type not null,
    name varchar(50) not null,
    code varchar(50) not null,
    display_name varchar(50) not null,
    seats integer,
    display_status boolean not null,
    foreign key (floor_id) references floors(id),
    foreign key (campus_code) references campus(code)
);
CREATE TABLE IF NOT EXISTS users (
    login varchar(128) not null primary key,
    type user_type not null,
    name varchar(100) not null
);
CREATE TABLE IF NOT EXISTS activity (
    id integer not null primary key,
    title varchar(128) not null
);
CREATE TABLE IF NOT EXISTS event (
    id serial primary key,
    room_id integer,
    start_time timestamp not null,
    end_time timestamp not null,
    foreign key (room_id) references rooms(id)
);
CREATE TABLE IF NOT EXISTS modules (
    id serial primary key,
    code varchar(9) not null,
    year integer not null,
    semester integer not null,
    campus_code varchar(3) not null,
    foreign key (campus_code) references campus(code),
    UNIQUE (code, year, semester, campus_code)
);
CREATE TABLE IF NOT EXISTS relation_user_event (
    id serial primary key,
    login varchar(128) not null,
    event_id integer not null,
    registration_type registration_type not null,
    registration_time timestamp not null,
    unsubscribe_time timestamp,
    present boolean,
    foreign key (login) references users(login),
    foreign key (event_id) references event(id),
    UNIQUE (login, event_id)
);
CREATE TABLE IF NOT EXISTS relation_campus_floor (
    campus_code varchar(3) not null,
    floor_id integer not null,
    foreign key (campus_code) references campus(code),
    foreign key (floor_id) references floors(id),
    UNIQUE (campus_code, floor_id)
);
CREATE TABLE IF NOT EXISTS relation_activity_event (
    activity_id integer not null,
    event_id integer not null,
    foreign key (activity_id) references activity(id),
    foreign key (event_id) references event(id),
    UNIQUE (activity_id, event_id)
);
CREATE TABLE IF NOT EXISTS relation_module_activity (
    module_id integer not null,
    activity_id integer not null,
    foreign key (module_id) references modules(id),
    foreign key (activity_id) references activity(id),
    UNIQUE (module_id, activity_id)
);
CREATE TABLE IF NOT EXISTS relation_user_module (
    login varchar(128) not null,
    module_id integer not null,
    foreign key (login) references users(login),
    foreign key (module_id) references modules(id),
    UNIQUE (login, module_id)
);
CREATE TABLE IF NOT EXISTS relation_user_campus (
    login varchar(128) not null,
    campus_code varchar(3) not null,
    foreign key (login) references users(login),
    foreign key (campus_code) references campus(code),
    UNIQUE (login, campus_code)
);
create view presence("login", "null", "absent", "present") as
	SELECT "login",
   sum(CASE WHEN present IS NULL THEN 1 ELSE 0 END) AS "null",
   sum(CASE WHEN present = false THEN 1 ELSE 0 END) AS "absent",
   sum(CASE WHEN present = true THEN 1 ELSE 0 END) AS "present"
FROM relation_user_event
GROUP BY "login";