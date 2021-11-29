-- Inserts the values that have no relation to each other --

-- Fleet --
INSERT INTO Fleet(id)
VALUES(1);
INSERT INTO Fleet(id)
VALUES(2);
INSERT INTO Fleet(id)
VALUES(3);

-- VesselType --
INSERT INTO VesselType(id)
VALUES(1);
INSERT INTO VesselType(id)
VALUES(2);

-- Captain --
INSERT INTO Captain(id)
VALUES(1);
INSERT INTO Captain(id)
VALUES(2);
INSERT INTO Captain(id)
VALUES(3);
INSERT INTO Captain(id)
VALUES(4);
INSERT INTO Captain(id)
VALUES(5);

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

-- Inserts tables with relations --

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

-- Ship --
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,captain_id)
VALUES(100000001,1,'Ship1',1000001,3,100,'SCS1',1,200,50,100,15,1);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,captain_id)
VALUES(100000002,2,'Ship2',1000002,3,100,'SCS2',2,200,50,100,15,2);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,captain_id)
VALUES(100000003,1,'Ship3',1000003,3,100,'SCS3',1,200,50,100,15,3);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,captain_id)
VALUES(100000004,3,'Ship4',1000004,3,100,'SCS4',2,200,50,100,15,4);
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft,captain_id)
VALUES(100000005,2,'Ship5',1000005,3,100,'SCS5',1,200,50,100,15,5);

-- Storage --
INSERT INTO Storage(identification, storage_type_id, name, continent, country, latitude,longitude)
VALUES(1, 1, 'Storage1', 'Europe', 'United Kingdom', 55, 3);
INSERT INTO Storage(identification, storage_type_id, name, continent, country, latitude,longitude)
VALUES(2, 1, 'Storage2', 'America', 'USA', 37, -96);
INSERT INTO Storage(identification, storage_type_id, name, continent, country, latitude,longitude)
VALUES(3, 1, 'Storage3', 'Asia', 'Japan', 36, 138);
INSERT INTO Storage(identification, storage_type_id, name, continent, country, latitude,longitude)
VALUES(4, 1, 'Storage4', 'Oceania', 'Australia', 25, 133);

-- Container --
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(1, null, 1, 4, 'COD1', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(2, null, 2, 4, 'COD2', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(3, 4, 3, 4, 'COD3', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(4, 3, 4, 4, 'COD4', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(5, 1, 5, 4, 'COD5', 200, 4000, 500, 1, 1);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(6, 2, 6, 4, 'COD6', 200, 4000, 500, 1, 0);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(7, 3, 7, 4, 'COD7', 200, 4000, 500, 1, 0);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(8, 4, 8, 4, 'COD8', 200, 4000, 500, 1, 0);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(9, 1, 9, 4, 'COD9', 200, 4000, 500, 1, 0);
INSERT INTO Container(num, storage_identification, csc_plate_serial_number, check_digit, iso_code, gross_weight, tare_weight, payload, max_volume, refrigerated_flag)
VALUES(10, 2, 10, 4, 'COD0', 200, 4000, 500, 1, 0);

-- Shipment --
INSERT INTO Shipment(container_num, storage_identification_origin, storage_identification_destination)
VALUES(1,1,2);
INSERT INTO Shipment(container_num, storage_identification_origin, storage_identification_destination)
VALUES(2,1,2);

-- CargoManifest --
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,1,TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,0,TO_TIMESTAMP('2020-05-21 17:48:36', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,1,TO_TIMESTAMP('2020-07-29 5:54:27', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,0,TO_TIMESTAMP('2020-07-29 21:26:32', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,1,TO_TIMESTAMP('2020-09-8 15:45:21', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,0,TO_TIMESTAMP('2020-09-9 6:19:45', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,1,TO_TIMESTAMP('2021-11-26 20:45:24', 'YYYY-MM-DD HH24:MI:SS'));
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,1,Null);
INSERT INTO CargoManifest(ship_mmsi, loading_flag, finishing_date_time)
VALUES(100000001,0,Null);

-- Container_CargoManifest --
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(5, 1, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 1, 1,2,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 1, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(9, 1, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 2, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 2, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(4, 3, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 3, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(10, 3, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(10, 4, 1,1,2);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 5, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 5, 1,2,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 5, 1,1,2);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(4, 5, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(5, 5, 3,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 5, 1,3,1);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 6, 1,3,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 6, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 7, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 7, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(1, 8, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(2, 8, 1,2,1);

INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(6, 9, 1,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(7, 9, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(8, 9, 1,1,2);


-- ShipTrip --
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 3, 4, TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-05-21 17:48:36', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 1, 3, TO_TIMESTAMP('2020-07-29 5:54:27', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-07-29 21:26:32', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 2, 1, TO_TIMESTAMP('2020-09-8 15:45:21', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-09-9 6:19:45', 'YYYY-MM-DD HH24:MI:SS'), 'finished');
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 1, 2, TO_TIMESTAMP('2021-11-26 20:45:24', 'YYYY-MM-DD HH24:MI:SS'), NULL, 'in progress');


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


SELECT 'CAPTAIN' AS "Table Name",COUNT(*) AS "Number Of Rows" FROM CAPTAIN UNION ALL
(SELECT 'CARGOMANIFEST',COUNT(*) FROM CARGOMANIFEST UNION  
SELECT 'CERTIFICATE',COUNT(*) FROM CERTIFICATE UNION  
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
SELECT 'VESSELTYPE',COUNT(*) FROM VESSELTYPE)

