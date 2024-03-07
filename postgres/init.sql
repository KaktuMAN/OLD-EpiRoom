CREATE TYPE room_type AS ENUM ('classroom', 'office', 'open_space', 'other');
CREATE TYPE user_type AS ENUM ('student', 'teacher', 'admin');
CREATE TYPE registration_type AS ENUM ('student', 'teacher');
CREATE TYPE presence_type AS ENUM ('null', 'absent', 'present', 'rdv');
CREATE TYPE role_type AS ENUM ('master', 'member');
CREATE TABLE IF NOT EXISTS floors (
    id serial primary key,
    campus_code varchar(3) not null,
    floor_number integer not null,
    name varchar(50) not null
);
CREATE TABLE IF NOT EXISTS campus (
    code varchar(3) not null primary key,
    name varchar(50),
    main_floor serial,
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
    activity_id integer not null,
    room_id integer,
    start_time timestamp not null,
    end_time timestamp not null,
    foreign key (activity_id) references activity(id),
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
CREATE TABLE IF NOT EXISTS "group" (
    id integer primary key,
    module_id integer not null,
    name varchar(128) not null,
    foreign key (module_id) references modules(id)
);
CREATE TABLE IF NOT EXISTS relation_user_group (
    login varchar(128) not null,
    group_id integer not null,
    role role_type not null,
    foreign key (login) references users(login),
    foreign key (group_id) references "group"(id),
    UNIQUE (login, group_id)
);
CREATE TABLE IF NOT EXISTS relation_user_event (
    id serial primary key,
    login varchar(128) not null,
    event_id integer not null,
    registration_type registration_type not null,
    registration_time timestamp not null,
    unsubscribe_time timestamp,
    status presence_type,
    group_id integer,
    foreign key (login) references users(login),
    foreign key (event_id) references event(id),
    foreign key (group_id) references "group"(id),
    UNIQUE (login, event_id),
    CONSTRAINT check_status_rdv CHECK (status = 'rdv' OR group_id IS NULL)
);
CREATE TABLE IF NOT EXISTS relation_campus_floor (
    campus_code varchar(3) not null,
    floor_id integer not null,
    foreign key (campus_code) references campus(code),
    foreign key (floor_id) references floors(id),
    UNIQUE (campus_code, floor_id)
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
CREATE VIEW presence("login", "null", "absent", "present") as
	SELECT "login",
       sum(CASE WHEN status = 'null' THEN 1 ELSE 0 END) AS "null",
       sum(CASE WHEN status = 'absent' THEN 1 ELSE 0 END) AS "absent",
       sum(CASE WHEN status = 'rdv' or status = 'present' THEN 1 ELSE 0 END) AS "present"
    FROM relation_user_event
GROUP BY "login";
CREATE VIEW activities("eventID", "activityTitle", "subscribedStudents", "presentStudents") AS
    SELECT event.id, activity.title, count(relation_user_event.login) as subscribed_students, sum(CASE WHEN relation_user_event.status = 'rdv' or relation_user_event.status = 'present' THEN 1 ELSE 0 END) as present_students
    FROM event
    JOIN activity ON event.activity_id = activity.id
    LEFT JOIN relation_user_event ON event.id = relation_user_event.event_id
GROUP BY event.id, activity.title;