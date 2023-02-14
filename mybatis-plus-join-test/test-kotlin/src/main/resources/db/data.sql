-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DELETE FROM `user`;

INSERT INTO `user` (id,user_id, `name`) VALUES (1,'user1','用户1');


DELETE FROM address;

INSERT INTO address (id, user_id, tel, address) VALUES ( 1, 'user1', '10000000001', '曹县01');
INSERT INTO address (id, user_id, tel, address) VALUES ( 2, 'user1', '10000000002', '曹县02');