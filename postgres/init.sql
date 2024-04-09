CREATE TYPE room_type AS ENUM ('classroom', 'office', 'openSpace', 'other');
CREATE TABLE IF NOT EXISTS floors (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    campus_code varchar(3) not null,
    floor integer not null,
    name varchar(50) not null,
    UNIQUE (campus_code, floor)
);
CREATE TABLE IF NOT EXISTS campus (
    code varchar(3) not null primary key,
    name varchar(50),
    main_floor integer,
    auto_login varchar(45),
    jenkins_token varchar(36),
    foreign key (main_floor) references floors(id)
);
CREATE TABLE IF NOT EXISTS rooms (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    floor_id integer not null,
    campus_code varchar(3) not null,
    type room_type not null,
    name varchar(50) not null,
    code varchar(50) not null,
    display_name varchar(50) not null,
    seats integer,
    display_status boolean not null,
    foreign key (floor_id) references floors(id),
    foreign key (campus_code) references campus(code),
    UNIQUE (floor_id, code)
);
CREATE TABLE IF NOT EXISTS linked_rooms (
    room_id integer not null,
    linked_room_id integer not null,
    foreign key (room_id) references rooms(id),
    foreign key (linked_room_id) references rooms(id),
    UNIQUE (room_id, linked_room_id)
);
CREATE TABLE IF NOT EXISTS modules (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    code varchar(9) not null,
    year integer not null,
    semester integer not null,
    campus_code varchar(3) not null,
    foreign key (campus_code) references campus(code),
    UNIQUE (code, year, semester, campus_code)
);
CREATE TABLE IF NOT EXISTS users (
    mail varchar(128) not null primary key,
    name varchar(128) not null,
    apiKey varchar(128)
);
CREATE TABLE IF NOT EXISTS permissions (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    permission varchar(128) not null
);
CREATE TABLE IF NOT EXISTS user_permissions (
    mail varchar(128) not null,
    permission_id integer not null,
    foreign key (mail) references users(mail),
    foreign key (permission_id) references permissions(id),
    UNIQUE (mail, permission_id)
);
CREATE TABLE IF NOT EXISTS activity (
    id integer not null primary key,
    title varchar(128) not null,
    module_id integer not null,
    foreign key (module_id) references modules(id)
);
CREATE TABLE IF NOT EXISTS event (
    id int GENERATED BY DEFAULT AS IDENTITY primary key,
    activity_id integer not null,
    room_id integer,
    start_time timestamp not null,
    end_time timestamp not null,
    campus_code varchar(3) not null,
    foreign key (activity_id) references activity(id),
    foreign key (room_id) references rooms(id),
    foreign key (campus_code) references campus(code)
);