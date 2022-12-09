-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DELETE FROM area;

INSERT INTO area (id, province, city, area, postcode, del) VALUES (10001, '北京市01', '北京01', '朝阳01', '80001', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10002, '北京市02', '北京02', '朝阳02', '80002', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10003, '北京市03', '北京03', '朝阳03', '80003', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10004, '北京市04', '北京04', '朝阳04', '80004', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10005, '北京市05', '北京05', '朝阳05', '80005', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10006, '北京市06', '北京06', '朝阳06', '80006', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10007, '北京市07', '北京07', '朝阳07', '80007', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10008, '北京市08', '北京08', '朝阳08', '80008', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10009, '北京市09', '北京09', '朝阳09', '80009', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10010, '北京市10', '北京10', '朝阳10', '80010', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10011, '北京市11', '北京11', '朝阳11', '80011', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10012, '北京市12', '北京12', '朝阳12', '80012', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10013, '北京市13', '北京13', '朝阳13', '80013', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10014, '北京市14', '北京14', '朝阳14', '80014', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10015, '北京市15', '北京15', '朝阳15', '80015', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10016, '北京市16', '北京16', '朝阳16', '80016', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10017, '北京市17', '北京17', '朝阳17', '80017', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10018, '北京市18', '北京18', '朝阳18', '80018', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10019, '北京市19', '北京19', '朝阳19', '80019', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10020, '北京市20', '北京20', '朝阳20', '80020', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10021, '北京市21', '北京21', '朝阳21', '80021', false);
INSERT INTO area (id, province, city, area, postcode, del) VALUES (10022, '北京市22', '北京22', '朝阳22', '80022', false);

DELETE FROM `user`;

INSERT INTO `user` (id, pid, `name`, `json`, `address_id`, sex, head_img, create_time, del) VALUES
( 1, 1, '张三 1', '{"id": 1,"name":"张三 1"}', 1, 1, 'https://url-01', '2022-01-01 12:00:00', false),
( 2, 1, '张三 2', '{"id": 2,"name":"张三 2"}', 1, 0, 'https://url-02', '2022-01-01 12:00:00', false),
( 3, 1, '张三 3', '{"id": 3,"name":"张三 3"}', 1, 0, 'https://url-03', '2022-01-01 12:00:00', false),
( 4, 1, '张三 4', '{"id": 4,"name":"张三 4"}', 1, 0, 'https://url-04', '2022-01-01 12:00:00', false),
( 5, 1, '张三 5', '{"id": 5,"name":"张三 5"}', 1, 0, 'https://url-05', '2022-01-01 12:00:00', false),
( 6, 1, '张三 6', '{"id": 6,"name":"张三 6"}', 1, 0, 'https://url-06', '2022-01-01 12:00:00', false),
( 7, 1, '张三 7', '{"id": 7,"name":"张三 7"}', 1, 0, 'https://url-07', '2022-01-01 12:00:00', false),
( 8, 1, '张三 8', '{"id": 8,"name":"张三 8"}', 1, 0, 'https://url-08', '2022-01-01 12:00:00', false),
( 9, 1, '张三 9', '{"id": 9,"name":"张三 9"}', 1, 0, 'https://url-09', '2022-01-01 12:00:00', false),
(10, 1, '张三10', '{"id":10,"name":"张三10"}', 1, 0, 'https://url-10', '2022-01-01 12:00:00', true ),
(11, 1, '张三11', '{"id":11,"name":"张三11"}', 1, 0, 'https://url-11', '2022-01-01 12:00:00', true ),
(12, 1, '张三12', '{"id":12,"name":"张三12"}', 1, 0, 'https://url-12', '2022-01-01 12:00:00', true ),
(13, 1, '张三13', '{"id":13,"name":"张三13"}', 1, 0, 'https://url-13', '2022-01-01 12:00:00', true ),
(14, 2, '张三14', '{"id":14,"name":"张三14"}', 1, 0, 'https://url-14', '2022-01-01 12:00:00', false),
(15, 2, '张三15', '{"id":15,"name":"张三15"}', 1, 0, 'https://url-15', '2022-01-01 12:00:00', false),
(16, 2, '张三16', '{"id":16,"name":"张三16"}', 1, 0, 'https://url-16', '2022-01-01 12:00:00', false),
(17, 2, '张三17', '{"id":17,"name":"张三17"}', 1, 0, 'https://url-17', '2022-01-01 12:00:00', false),
(18, 2, '张三18', '{"id":18,"name":"张三18"}', 1, 0, 'https://url-18', '2022-01-01 12:00:00', false),
(19, 2, '张三19', '{"id":19,"name":"张三19"}', 1, 0, 'https://url-19', '2022-01-01 12:00:00', true ),
(20, 2, '张三20', '{"id":20,"name":"张三20"}', 1, 0, 'https://url-20', '2022-01-01 12:00:00', true ),
(21, 2, '张三21', '{"id":21,"name":"张三21"}', 1, 0, 'https://url-21', '2022-01-01 12:00:00', true ),
(22, 2, '张三22', '{"id":22,"name":"张三22"}', 1, 0, 'https://url-22', '2022-01-01 12:00:00', true );


DELETE FROM address;

INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 1, 1, 10001, '10000000001', '曹县01', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 2, 1, 10002, '10000000002', '曹县02', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 3, 1, 10003, '10000000003', '曹县03', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 4, 1, 10004, '10000000004', '曹县04', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 5, 1, 10005, '10000000005', '曹县05', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 6, 1, 10006, '10000000006', '曹县06', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 7, 1, 10007, '10000000007', '曹县07', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 8, 1, 10008, '10000000008', '曹县08', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 9, 1, 10009, '10000000009', '曹县09', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (10,10, 10010, '10000000010', '曹县10', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (11,11, 10011, '10000000011', '曹县11', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (12,12, 10012, '10000000012', '曹县12', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (13,13, 10013, '10000000013', '曹县13', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (14,14, 10014, '10000000014', '曹县14', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (15,15, 10015, '10000000015', '曹县15', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (16,16, 10016, '10000000016', '曹县16', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (17,17, 10017, '10000000017', '曹县17', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (18,18, 10018, '10000000018', '曹县18', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (19,19, 10019, '10000000019', '曹县19', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (20,20, 10020, '10000000020', '曹县20', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (21,21, 10021, '10000000021', '曹县21', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (22,22, 10022, '10000000022', '曹县22', false);

