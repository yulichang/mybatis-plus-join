-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile


DROP TABLE IF EXISTS table_a;
create table table_a
(
    id     int auto_increment
        primary key,
    `name` varchar(255) null
);

DROP TABLE IF EXISTS table_b;
create table table_b
(
    id     int auto_increment
        primary key,
    `aid`  int not null,
    `name` varchar(255) null
);

DROP TABLE IF EXISTS table_c;
create table table_c
(
    id     int auto_increment
        primary key,
    `bid`  int not null,
    `name` varchar(255) null
);

DROP TABLE IF EXISTS table_d;
create table table_d
(
    id     int auto_increment
        primary key,
    `cid`  int not null,
    `name` varchar(255) null
);

DROP TABLE IF EXISTS table_e;
create table table_e
(
    id     int auto_increment
        primary key,
    `did`  int not null,
    `name` varchar(255) null
);