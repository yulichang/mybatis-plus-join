-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DELETE FROM table_a;
insert into table_a (id, `name`) values (1, 'tableA1');
insert into table_a (id, `name`) values (2, 'tableA2');
insert into table_a (id, `name`) values (3, 'tableA3');

DELETE FROM table_b;
insert into table_b (id, aid, `name`) values (1, 1, 'tableB1');
insert into table_b (id, aid, `name`) values (2, 1, 'tableB2');
insert into table_b (id, aid, `name`) values (3, 2, 'tableB3');

DELETE FROM table_c;
insert into table_c (id, bid, `name`) values (1, 1, 'tableC1');
insert into table_c (id, bid, `name`) values (2, 1, 'tableC2');
insert into table_c (id, bid, `name`) values (3, 2, 'tableC3');

DELETE FROM table_d;
insert into table_d (id, cid, `name`) values (1, 1, 'tableD1');
insert into table_d (id, cid, `name`) values (2, 1, 'tableD2');
insert into table_d (id, cid, `name`) values (3, 2, 'tableD3');

DELETE FROM table_e;
insert into table_e (id, did, `name`) values (1, 1, 'tableE1');
insert into table_e (id, did, `name`) values (2, 1, 'tableE2');
insert into table_e (id, did, `name`) values (3, 2, 'tableE3');

DELETE FROM table_t;
insert into table_t (id, aid1, aid2, `name`) values (1, 1, 2, 'tableT1');
insert into table_t (id, aid1, aid2, `name`) values (2, 1, 2, 'tableT2');
insert into table_t (id, aid1, aid2, `name`) values (3, 2, 3, 'tableT3');
