-- noinspection SqlDialectInspectionForFile

-- noinspection SqlNoDataSourceInspectionForFile

DELETE FROM area;

INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10001, '北京市01', '北京01', '朝阳01', '80001', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10002, '北京市02', '北京02', '朝阳02', '80002', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10003, '北京市03', '北京03', '朝阳03', '80003', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10004, '北京市04', '北京04', '朝阳04', '80004', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10005, '北京市05', '北京05', '朝阳05', '80005', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10006, '北京市06', '北京06', '朝阳06', '80006', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10007, '北京市07', '北京07', '朝阳07', '80007', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10008, '北京市08', '北京08', '朝阳08', '80008', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10009, '北京市09', '北京09', '朝阳09', '80009', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10010, '北京市10', '北京10', '朝阳10', '80010', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10011, '北京市11', '北京11', '朝阳11', '80011', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10012, '北京市12', '北京12', '朝阳12', '80012', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10013, '北京市13', '北京13', '朝阳13', '80013', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10014, '北京市14', '北京14', '朝阳14', '80014', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10015, '北京市15', '北京15', '朝阳15', '80015', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10016, '北京市16', '北京16', '朝阳16', '80016', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10017, '北京市17', '北京17', '朝阳17', '80017', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10018, '北京市18', '北京18', '朝阳18', '80018', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10019, '北京市19', '北京19', '朝阳19', '80019', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10020, '北京市20', '北京20', '朝阳20', '80020', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10021, '北京市21', '北京21', '朝阳21', '80021', false);
INSERT INTO area (area_id, province, city_id, area, postcode, del) VALUES (10022, '北京市22', '北京22', '朝阳22', '80022', false);

DELETE FROM `user`;

INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 1, null, '{"aa":"aaa","bb":"bbb"}', 1, 1, 'https://url-01', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 2, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-02', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 3, 2, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-03', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 4, 3, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-04', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 5, 4, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-05', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 6, 5, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-06', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 7, 6, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-07', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 8, 7, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-08', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES ( 9, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-09', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (10, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-10', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (11, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-11', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (12, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-12', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (13, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-13', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (14, 7, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-14', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (15, 7, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-15', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (16, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-16', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (17, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-17', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (18, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-18', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (19, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-19', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (20, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-20', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (21, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-21', false);
INSERT INTO `user` (id, pid, `name`, `address_id`, sex, head_img, del) VALUES (22, 1, '{"aa":"aaa","bb":"bbb"}', 1, 0, 'https://url-22', false);


DELETE FROM address;

INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 1, 1, 10001, '北京01', '10000000001', '人民广场01', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 2, 1, 10002, '北京02', '10000000002', '人民广场02', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 3, 1, 10003, '北京03', '10000000003', '人民广场03', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 4, 1, 10004, '北京04', '10000000004', '人民广场04', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 5, 1, 10005, '北京05', '10000000005', '人民广场05', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 6, 1, 10006, '北京06', '10000000006', '人民广场06', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 7, 1, 10007, '北京07', '10000000007', '人民广场07', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 8, 1, 10008, '北京08', '10000000008', '人民广场08', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES ( 9, 1, 10009, '北京09', '10000000009', '人民广场09', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (10,10, 10010, '北京10', '10000000010', '人民广场10', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (11,11, 10011, '北京11', '10000000011', '人民广场11', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (12,12, 10012, '北京12', '10000000012', '人民广场12', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (13,13, 10013, '北京13', '10000000013', '人民广场13', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (14,14, 10014, '北京14', '10000000014', '人民广场14', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (15,15, 10015, '北京15', '10000000015', '人民广场15', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (16,16, 10016, '北京16', '10000000016', '人民广场16', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (17,17, 10017, '北京17', '10000000017', '人民广场17', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (18,18, 10018, '北京18', '10000000018', '人民广场18', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (19,19, 10019, '北京19', '10000000019', '人民广场19', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (20,20, 10020, '北京20', '10000000020', '人民广场20', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (21,21, 10021, '北京21', '10000000021', '人民广场21', false);
INSERT INTO address (id, user_id, area_id, city, tel, address, del) VALUES (22,22, 10022, '北京22', '10000000022', '人民广场22', false);