-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE IF EXISTS area;

create table area
(
    id       int auto_increment
        primary key,
    province varchar(255) null,
    city     varchar(255) null,
    area     varchar(255) null,
    postcode varchar(255) null,
    del      bit
);

DROP TABLE IF EXISTS `user`;

create table `user`
(
    id           int auto_increment
        primary key,
    `pid`        int          not null,
    `name`       varchar(255) not null,
    `json`       varchar(255) not null,
    `address_id` int          not null,
    `address_id2` int          not null,
    sex          tinyint      not null,
    head_img     varchar(255) not null,
    create_time  datetime not null,
    create_by  int not null,
    update_by  int not null,
    del          bit
);

DROP TABLE IF EXISTS address;

create table address
(
    id      int auto_increment
        primary key,
    user_id int null,
    area_id int null,
    tel     varchar(255) null,
    address varchar(255) null,
    del     bit
);

create table user_dto
(
    id        int auto_increment
        primary key,
    user_id   int not null,
    create_by int not null,
    update_by int not null,
    del       bit null
);
