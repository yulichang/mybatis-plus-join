-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS `user`;

create table `user`
(
    id      int auto_increment
        primary key,
    user_id     varchar(255) not null ,
    `name`       varchar(255) not null
);

DROP TABLE IF EXISTS address;

create table address
(
    id      int auto_increment
        primary key,
    user_id varchar(255) null,
    tel     varchar(255) null,
    address varchar(255) null
);