CREATE TYPE room_type AS ENUM ('CLASSROOM', 'OFFICE', 'OPENSPACE', 'MULTIROOM', 'OTHER');
CREATE CAST (CHARACTER VARYING as room_type) WITH INOUT AS IMPLICIT;
CREATE TABLE IF NOT EXISTS floors (
    id SERIAL primary key,
    campus_code varchar(3) not null,
    floor integer not null,
    name TEXT not null,
    UNIQUE (campus_code, floor)
);
CREATE TABLE IF NOT EXISTS campus (
    code varchar(3) not null primary key,
    name TEXT,
    main_floor integer,
    auto_login varchar(45),
    jenkins_token varchar(36),
    foreign key (main_floor) references floors(id)
);
CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL primary key,
    floor_id integer not null,
    campus_code varchar(3) not null,
    type room_type not null,
    name TEXT not null,
    code TEXT not null,
    display_name TEXT not null,
    seats integer,
    display_status boolean not null,
    foreign key (floor_id) references floors(id),
    foreign key (campus_code) references campus(code),
    UNIQUE (campus_code, code)
);
CREATE TABLE IF NOT EXISTS linked_rooms (
    room_id integer not null,
    linked_room_id integer not null,
    foreign key (room_id) references rooms(id),
    foreign key (linked_room_id) references rooms(id),
    UNIQUE (room_id, linked_room_id)
);
CREATE TABLE IF NOT EXISTS modules (
    id SERIAL primary key,
    code varchar(9) not null,
    year integer not null,
    semester integer not null,
    campus_code varchar(3) not null,
    foreign key (campus_code) references campus(code),
    UNIQUE (code, year, semester, campus_code)
);
CREATE TABLE IF NOT EXISTS users (
    mail TEXT not null primary key,
    name TEXT not null,
    api_key varchar(36),
    first_login timestamp not null,
    last_login timestamp not null
);
CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL primary key,
    permission varchar(128) not null
);
CREATE TABLE IF NOT EXISTS user_permissions (
    mail TEXT not null,
    permission_id integer not null,
    foreign key (mail) references users(mail),
    foreign key (permission_id) references permissions(id),
    UNIQUE (mail, permission_id)
);
CREATE TABLE IF NOT EXISTS activities (
    id SERIAL primary key,
    title TEXT not null,
    module_id integer not null,
    foreign key (module_id) references modules(id)
);
CREATE TABLE IF NOT EXISTS events (
    id SERIAL primary key,
    activity_id integer not null,
    room_id integer,
    start_time timestamp not null,
    end_time timestamp not null,
    campus_code varchar(3) not null,
    foreign key (activity_id) references activities(id),
    foreign key (room_id) references rooms(id),
    foreign key (campus_code) references campus(code)
);
CREATE TABLE IF NOT EXISTS status_messages (
    id SERIAL primary key,
    campus_code varchar(3) not null,
    creator_mail TEXT not null,
    message TEXT not null,
    start_date timestamp not null,
    end_date timestamp not null,
    foreign key (campus_code) references campus(code),
    foreign key (creator_mail) references users(mail)
);