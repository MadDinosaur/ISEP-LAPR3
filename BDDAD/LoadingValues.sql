-- Inserts the values that have no relation to each other --

-- Role --
INSERT INTO Role (name)
VALUES ('Fleet Manager');
INSERT INTO Role(name)
VALUES ('Ship Captain');
INSERT INTO Role (name)
VALUES ('Traffic Manager');
INSERT INTO Role (name)
VALUES ('Port Manager');
INSERT INTO Role (name)
VALUES ('Warehouse Manager');

-- Truck --
INSERT INTO Truck(id)
VALUES(1);
INSERT INTO Truck(id)
VALUES(2);
INSERT INTO Truck(id)
VALUES(3);

-- VesselType --
INSERT INTO VesselType(id)
VALUES(1);
INSERT INTO VesselType(id)
VALUES(2);

-- StorageType --
INSERT INTO StorageType(name)
VALUES('Port');
INSERT INTO StorageType(name)
VALUES('WareHouse');

-- Certificate --
INSERT INTO Certificate(id, name)
VALUES(1, 'Certificate1');
INSERT INTO Certificate(id, name)
VALUES(2, 'Certificate2');
INSERT INTO Certificate(id, name)
VALUES(3, 'Certificate3');

--  CscPlate --
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(1, 'freeze', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 1, 1, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(2, 'freeze', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 2, 2, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(3, 'freeze', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 3, 3, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(4, 'freeze', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 4, 4, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(5, 'freeze', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 5, 5, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(6, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 6, 6, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(7, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 7, 7, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(8, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 8, 8, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(9, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 9, 9, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(10, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 10, 10, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);
INSERT INTO CscPlate(serial_number, rules, model, manufacturer_name, owner_name, owner_address, furnigation, approval_number, acep_number, date_manufactured, max_gross_mass, stacking_weight,racking_test)
VALUES(11, 'Not frozen', 'Alloy', 'Daikin Industries', 'Bob', 'America', 'furnigation', 11, 11, TO_DATE('01/01/2000','DD/MM/YYYY'), 400, 4000, 51);

-- Country --
INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude)
VALUES('America','GY','GUY','Guyana',786.5,'Georgetown',6.8,-58.15);
INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude)
VALUES('America','VE','VEN','Venezuela',31.02,'Caracas',10.48333333,-66.866667);
INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude)
VALUES('Europe','LU','LUX','Luxembourg',0.59,'Luxembourg',49.6,6.116667);
INSERT INTO Country(continent,alpha2,alpha3,country,population,capital,latitude,longitude)
VALUES('Europe','AT','AUT','Austria',8.77,'Vienna',48.2,16.366667);


-- Inserts tables with relations --
-- SystemUser --
INSERT INTO SystemUser(name, email)
VALUES ('ExampleCaptain1', 'captain1@ship.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleCaptain2', 'captain2@ship.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleCaptain3', 'captain3@ship.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleCaptain4', 'captain4@ship.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleCaptain5', 'captain5@ship.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleFleetManager1', 'manager1@fleet.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleFleetManager2', 'manager2@fleet.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleFleetManager3', 'manager3@fleet.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExamplePortManager', 'manager1@port.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleWarehouseMngr', 'manager1@warehouse.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleClient', 'example@client.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExamplePortManager2', 'manager2@port.com');
INSERT INTO SystemUser(name, email)
VALUES ('ExampleFleetManager4', 'manager4@fleet.com');

-- Employees --
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('1', 2);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('2', 2);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('3', 2);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('4', 2);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('5', 2);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('6', 1);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('7', 1);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('8', 1);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('9', 4);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('10', 5);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('12', 4);
INSERT INTO EMPLOYEE (SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('13', 1);

-- Client --
INSERT INTO CLIENT (SYSTEM_USER_CODE_CLIENT)
VALUES ('11');

-- Fleet --
INSERT INTO Fleet(id, system_user_code_manager)
VALUES(1, '6');
INSERT INTO Fleet(id, system_user_code_manager)
VALUES(2, '7');
INSERT INTO Fleet(id, system_user_code_manager)
VALUES(3, '8');

-- CscPlate_Certificate --
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(1, 1);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(2, 1);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(3, 2);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(4, 2);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(5, 2);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(6, 2);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(7, 3);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(8, 3);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(9, 3);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(10, 2);
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES(11, 2);

-- Ship --
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,system_user_code_captain)
VALUES(100000001,1,'Ship1',1000001,3,100,'SCS1',1,200,50,100,15,1);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,system_user_code_captain)
VALUES(100000002,2,'Ship2',1000002,3,100,'SCS2',2,200,50,100,15,2);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,system_user_code_captain)
VALUES(100000003,1,'Ship3',1000003,3,100,'SCS3',1,200,50,100,15,3);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,system_user_code_captain)
VALUES(100000004,3,'Ship4',1000004,3,100,'SCS4',2,200,50,100,15,4);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,system_user_code_captain)
VALUES(100000005,2,'Ship5',1000005,3,100,'SCS5',1,200,50,100,15,5);

-- Storage --
INSERT INTO Storage(identification, storage_type_id, name, max_volume, country_name, latitude,longitude, SYSTEM_USER_CODE_MANAGER)
VALUES(1, 1, 'Storage1', 500, 'Guyana', 6, -57, '9');
INSERT INTO Storage(identification, storage_type_id, name, max_volume, country_name, latitude,longitude, SYSTEM_USER_CODE_MANAGER)
VALUES(2, 1, 'Storage2', 600,'Venezuela', 11, -67, '9');
INSERT INTO Storage(identification, storage_type_id, name, max_volume, country_name, latitude,longitude, SYSTEM_USER_CODE_MANAGER)
VALUES(3, 1, 'Storage3', 700, 'Luxembourg', 50, 5.7, '9');
INSERT INTO Storage(identification, storage_type_id, name, max_volume, country_name, latitude,longitude, SYSTEM_USER_CODE_MANAGER)
VALUES(4, 1, 'Storage4', 450, 'Austria', 50, 18, '9');

-- Container --
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(1, 1, 4, 'COD1', 200, 4000, 500, 5, 1);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(2, 2, 4, 'COD2', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(3, 3, 4, 'COD3', 200, 4000, 500, 26, 1);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(4, 4, 4, 'COD4', 200, 4000, 500, 25, 1);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(5, 5, 4, 'COD5', 200, 4000, 500, 6, 1);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(6, 6, 4, 'COD6', 200, 4000, 500, 3, 0);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(7, 7, 4, 'COD7', 200, 4000, 500, 4, 0);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(8, 8, 4, 'COD8', 200, 4000, 500, 6, 0);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(9, 9, 4, 'COD9', 200, 4000, 500, 13, 0);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(10, 10, 4, 'COD0', 200, 4000, 500, 6, 0);
INSERT INTO Container(num, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(11, 11, 4, 'COF0', 200, 4000, 500, 500, 0);

-- Shipment --
INSERT INTO Shipment(container_num, storage_identification_origin, storage_identification_destination, system_user_code_client)
VALUES(1,1,2, 11);
INSERT INTO Shipment(container_num, storage_identification_origin, storage_identification_destination, system_user_code_client)
VALUES(2,1,2, 11);
INSERT INTO SHIPMENT (CONTAINER_NUM, STORAGE_IDENTIFICATION_ORIGIN, STORAGE_IDENTIFICATION_DESTINATION, SYSTEM_USER_CODE_CLIENT)
VALUES (3, 1, 3, 11);

-- CargoManifestPartial --
INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,1, 2);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,0, 4);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,1, 4);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,0, 3);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,1, 3);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,0, 1);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,1, 1);

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,1, 2);
INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001,0, 2);

INSERT INTO CARGOMANIFEST_PARTIAL (SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG)
VALUES (100000001, 1, 1);
INSERT INTO CARGOMANIFEST_PARTIAL (SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG)
VALUES (100000001, 2, 0);
INSERT INTO CARGOMANIFEST_PARTIAL (TRUCK_ID, STORAGE_IDENTIFICATION, LOADING_FLAG)
VALUES (1, 2, 1);
INSERT INTO CARGOMANIFEST_PARTIAL (TRUCK_ID, STORAGE_IDENTIFICATION, LOADING_FLAG)
VALUES (1, 3, 0);

INSERT INTO CARGOMANIFEST_PARTIAL (SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG)
VALUES (100000003, 1, 1);

-- Container_CargoManifest --
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 1, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 1, 1,2,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(8, 1, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(9, 1, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(8, 2, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(9, 2, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(4, 3, 2,1,2);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(7, 3, 1,1,2);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(10, 3, 1,2,2);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(7, 4, 1,2,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(10, 4, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(5, 5, 3,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 5, 1,3,1);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 6, 1,3,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 6, 1,2,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(4, 6, 1,2,2);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(5, 6, 2,2,1);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 7, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 8, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 8, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 9, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(7, 9, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(8, 9, 1,1,2);

INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
VALUES (3, 10, 0,0,0);
INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
VALUES (3, 11, 0,0,0);
INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
VALUES (3, 12, 0,0,0);
INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
VALUES (3, 13, 0,0,0);

INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
VALUES (4, 14, 0,0,0);

-- CargoManifestPartial (Conclusion) --
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 1;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-05-21 17:48:39', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 2;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-07-29 5:54:27', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 3;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-07-29 21:26:39', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 4;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-09-8 15:45:21', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 5;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2020-09-9 6:19:49', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 6;
UPDATE CargoManifest_Partial SET finishing_date_time = TO_TIMESTAMP('2021-11-26 20:45:24', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE id = 7;
UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = TO_TIMESTAMP('2021-12-01 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE ID = 10;
UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = TO_TIMESTAMP('2021-12-02 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE ID = 11;
UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = TO_TIMESTAMP('2021-12-03 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE ID = 12;
UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = TO_TIMESTAMP('2021-12-04 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE ID = 13;
UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = TO_TIMESTAMP('2021-12-3 23:23:51', 'YYYY-MM-DD HH24:MI:SS'), status = 'finished' WHERE ID = 14;

-- ShipTrip --
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 2, 4, TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-05-21 17:48:36', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 4, 3, TO_TIMESTAMP('2020-07-29 5:54:27', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-07-29 21:26:32', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 3, 1, TO_TIMESTAMP('2020-09-8 15:45:21', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-09-9 6:19:45', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 1, 2, TO_TIMESTAMP('2021-11-26 20:45:24', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2021-12-9 6:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'in progress');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000002, 2, 1, TO_TIMESTAMP('2021-12-10 21:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2021-12-12 12:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'not started');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000003, 1, 4, TO_TIMESTAMP('2021-12-3 23:23:51', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2021-12-5 15:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000004, 2, 3, TO_TIMESTAMP('2021-12-1 11:54:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2021-12-6 17:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'in progress');

-- DynamicData --
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-05-20 9:45:51', 'YYYY-MM-DD HH24:MI:SS'), 34, 135, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-05-20 13:05:41', 'YYYY-MM-DD HH24:MI:SS'), 32, 134, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-05-20 23:42:51', 'YYYY-MM-DD HH24:MI:SS'), 31, 133, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-05-21 13:41:58', 'YYYY-MM-DD HH24:MI:SS'), 28, 133, 10, 10, 250, 'A');

INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-07-29 7:32:51', 'YYYY-MM-DD HH24:MI:SS'), 50, 23, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-07-29 11:06:21', 'YYYY-MM-DD HH24:MI:SS'), 43, 51, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-07-29 15:23:45', 'YYYY-MM-DD HH24:MI:SS'), 31, 103, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-07-29 21:13:55', 'YYYY-MM-DD HH24:MI:SS'), 32, 128, 10, 10, 250, 'A');

INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-09-8 17:25:23', 'YYYY-MM-DD HH24:MI:SS'), 39, -63, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-09-9 01:54:32', 'YYYY-MM-DD HH24:MI:SS'), 47, -19, 10, 10, 250, 'A');
INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2020-09-8 04:58:32', 'YYYY-MM-DD HH24:MI:SS'), 53, 1, 10, 10, 250, 'A');

INSERT INTO DynamicData(ship_mmsi, base_date_time, latitude, longitude, sog, cog, heading, transceiver_class)
VALUES(100000001, TO_TIMESTAMP('2021-11-26 23:48:27', 'YYYY-MM-DD HH24:MI:SS'), 52, -1, 10, 10, 250, 'A');



SELECT 'CARGOMANIFEST_PARTIAL' AS "Table Name" ,COUNT(*) AS "Number Of Rows" FROM CARGOMANIFEST_PARTIAL UNION ALL
(SELECT 'CERTIFICATE',COUNT(*) FROM CERTIFICATE UNION
SELECT 'CONTAINER',COUNT(*) FROM CONTAINER UNION
SELECT 'CONTAINER_CARGOMANIFEST',COUNT(*) FROM CONTAINER_CARGOMANIFEST UNION
SELECT 'CSCPLATE',COUNT(*) FROM CSCPLATE UNION
SELECT 'CSCPLATE_CERTIFICATE',COUNT(*) FROM CSCPLATE_CERTIFICATE UNION
SELECT 'DYNAMICDATA',COUNT(*) FROM DYNAMICDATA UNION
SELECT 'FLEET',COUNT(*) FROM FLEET UNION
SELECT 'SHIP',COUNT(*) FROM SHIP UNION
SELECT 'SHIPMENT',COUNT(*) FROM SHIPMENT UNION
SELECT 'SHIPTRIP',COUNT(*) FROM SHIPTRIP UNION
SELECT 'STORAGE',COUNT(*) FROM STORAGE UNION
SELECT 'STORAGETYPE',COUNT(*) FROM STORAGETYPE UNION
SELECT 'VESSELTYPE',COUNT(*) FROM VESSELTYPE UNION
SELECT 'COUNTRY',COUNT(*) FROM COUNTRY UNION
SELECT 'BORDER',COUNT(*) FROM BORDER UNION
SELECT 'STORAGE_PATH',COUNT(*) FROM STORAGE_PATH UNION
SELECT 'CARGOMANIFEST_FULL',COUNT(*) FROM CARGOMANIFEST_FULL UNION
SELECT 'ROLE',COUNT(*) FROM ROLE UNION
SELECT 'SYSTEMUSER',COUNT(*) FROM SYSTEMUSER UNION
SELECT 'TRUCK',COUNT(*) FROM TRUCK)


