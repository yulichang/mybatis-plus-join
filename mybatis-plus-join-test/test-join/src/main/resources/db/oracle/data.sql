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

DELETE FROM "user";

INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 1, 1, '张三 1', '[{"id": 1,"name":"张三 1"}]', 1, 2, 1, 'https://url-01', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 2, 1, '张三 2', '[{"id": 2,"name":"张三 2"}]', 1, 2, 0, 'https://url-02', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 2, 3, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 3, 1, '张三 3', '[{"id": 3,"name":"张三 3"}]', 1, 2, 0, 'https://url-03', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 3, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 4, 1, '张三 4', '[{"id": 4,"name":"张三 4"}]', 1, 2, 0, 'https://url-04', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 9, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 5, 1, '张三 5', '[{"id": 5,"name":"张三 5"}]', 1, 2, 0, 'https://url-05', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 6, 1, '张三 6', '[{"id": 6,"name":"张三 6"}]', 1, 2, 0, 'https://url-06', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 7, 1, '张三 7', '[{"id": 7,"name":"张三 7"}]', 1, 2, 0, 'https://url-07', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 8, 1, '张三 8', '[{"id": 8,"name":"张三 8"}]', 1, 2, 0, 'https://url-08', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES ( 9, 1, '张三 9', '[{"id": 9,"name":"张三 9"}]', 1, 2, 0, 'https://url-09', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (10, 1, '张三10', '[{"id":10,"name":"张三10"}]', 1, 2, 0, 'https://url-10', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (11, 1, '张三11', '[{"id":11,"name":"张三11"}]', 1, 2, 0, 'https://url-11', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (12, 1, '张三12', '[{"id":12,"name":"张三12"}]', 1, 2, 0, 'https://url-12', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (13, 1, '张三13', '[{"id":13,"name":"张三13"}]', 1, 2, 0, 'https://url-13', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (14, 2, '张三14', '[{"id":14,"name":"张三14"}]', 1, 2, 0, 'https://url-14', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (15, 2, '张三15', '[{"id":15,"name":"张三15"}]', 1, 2, 0, 'https://url-15', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (16, 2, '张三16', '[{"id":16,"name":"张三16"}]', 1, 2, 0, 'https://url-16', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (17, 2, '张三17', '[{"id":17,"name":"张三17"}]', 1, 2, 0, 'https://url-17', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (18, 2, '张三18', '[{"id":18,"name":"张三18"}]', 1, 2, 0, 'https://url-18', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, false);
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (19, 2, '张三19', '[{"id":19,"name":"张三19"}]', 1, 2, 0, 'https://url-19', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (20, 2, '张三20', '[{"id":20,"name":"张三20"}]', 1, 2, 0, 'https://url-20', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (21, 2, '张三21', '[{"id":21,"name":"张三21"}]', 1, 2, 0, 'https://url-21', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );
INSERT INTO "user" (id, pid, "name", "json", address_id, address_id2 , sex, head_img, create_time, create_by, update_by, del) VALUES (22, 2, '张三22', '[{"id":22,"name":"张三22"}]', 1, 2, 0, 'https://url-22', TO_TIMESTAMP('2022-01-01 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 2, true );


DELETE FROM address;

INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 1, 1, 10001, '10000000001', '朝阳01', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 2, 1, 10002, '10000000002', '朝阳02', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 3, 1, 10003, '10000000003', '朝阳03', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 4, 1, 10004, '10000000004', '朝阳04', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 5, 1, 10005, '10000000005', '朝阳05', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 6, 1, 10006, '10000000006', '朝阳06', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 7, 1, 10007, '10000000007', '朝阳07', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 8, 1, 10008, '10000000008', '朝阳08', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES ( 9, 1, 10009, '10000000009', '朝阳09', true );
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (10,10, 10010, '10000000010', '朝阳10', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (11,11, 10011, '10000000011', '朝阳11', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (12,12, 10012, '10000000012', '朝阳12', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (13,13, 10013, '10000000013', '朝阳13', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (14,14, 10014, '10000000014', '朝阳14', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (15,15, 10015, '10000000015', '朝阳15', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (16,16, 10016, '10000000016', '朝阳16', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (17,17, 10017, '10000000017', '朝阳17', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (18,18, 10018, '10000000018', '朝阳18', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (19,19, 10019, '10000000019', '朝阳19', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (20,20, 10020, '10000000020', '朝阳20', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (21,21, 10021, '10000000021', '朝阳21', false);
INSERT INTO address (id, user_id, area_id, tel, address, del) VALUES (22,22, 10022, '10000000022', '朝阳22', false);

DELETE FROM user_dto;
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (1,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (2,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (3,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (4,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (5,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (6,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (7,1, 2, 3, false);
INSERT INTO user_dto (id, user_id, create_by, update_by, del) VALUES (8,1, 2, 3, false);

DELETE FROM order_t;
INSERT INTO order_t (id, user_id, age, name) VALUES (1, 1, 8, '1,8');
INSERT INTO order_t (id, user_id, age, name) VALUES (2, 2, 7, '2,7');
INSERT INTO order_t (id, user_id, age, name) VALUES (3, 3, 6, '3,6');
INSERT INTO order_t (id, user_id, age, name) VALUES (4, 4, 5, '4,5');
INSERT INTO order_t (id, user_id, age, name) VALUES (5, 5, 4, '5,4');
INSERT INTO order_t (id, user_id, age, name) VALUES (6, 6, 3, '6,3');
INSERT INTO order_t (id, user_id, age, name) VALUES (7, 7, 2, '7,2');
INSERT INTO order_t (id, user_id, age, name) VALUES (8, 8, 1, '8,1');

DELETE FROM user_tenant;
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (1, 1, 1);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (2, 2, 1);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (3, 3, 1);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (4, 4, 1);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (5, 5, 1);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (6, 6, 2);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (7, 7, 2);
INSERT INTO user_tenant (id,user_id, tenant_id) VALUES (8, 8, 2);

DELETE FROM user_tenanta;
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (1, 1, 1, '中文字段1');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (2, 2, 1, '中文字段2');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (3, 3, 1, '中文字段3');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (4, 4, 1, '中文字段4');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (5, 5, 1, '中文字段5');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (6, 6, 2, '中文字段6');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (7, 7, 2, '中文字段7');
INSERT INTO user_tenanta (id,user_id, tenant_id, 中文字段) VALUES (8, 8, 2, '中文字段8');

DELETE FROM tablea;
INSERT INTO tablea (id,map_col, entry_col,list_col) VALUES
(1, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(2, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(3, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(4, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(5, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(6, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(7, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]'),
(8, '{"id":1}', '{"name":"Tom"}', '["jerry","Spike"]');