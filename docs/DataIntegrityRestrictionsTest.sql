-- REFERENTIAL RESTRICTIONS
-- Tests if the foreign key refers to a primary key value of some table in the database.

-- CONSTRAINT fkStorageTypeId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (1,1,1,'TestStorage','TestContinent','TestCountry',91.0,181.0);
-- CONSTRAINT fkStorageSystemUserManagerId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO StorageType(name) VALUES ('TestStorageType');
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (1,2,1,'TestStorage','TestContinent','TestCountry',91.0,181.0);

-- CONSTRAINT fkContainerCscPlateSerialNumber -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (1,2,1,0,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT fkContainerStorageIdentification -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (1,'TST1234','TST1234','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1234','TST1234',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (1,2,1,0,'TST1',9999999,9999999,999999,999.9,1);

-- CONSTRAINT fkContainerCargoManifestCargoManifestId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Container_CargoManifest(container_num,cargo_manifest_id,container_position_x,container_position_y,container_position_z) VALUES(2,1,0,0,0);
-- CONSTRAINT fkContainerCargoManifestCargoManifestId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Truck(id) VALUES (1);
INSERT INTO CargoManifest(truck_id,ship_mmsi,loading_flag) VALUES (1,NULL,1);
INSERT INTO Container_CargoManifest(container_num,cargo_manifest_id,container_position_x,container_position_y,container_position_z) VALUES(2,1,0,0,0);

-- CONSTRAINT fkStorageUserStaffSystemUserId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Storage_User_Staff(storage_identification, system_user_id) VALUES (2,2);
-- CONSTRAINT fkStorageUserStaffStorageIdentification -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Role(name) VALUES ('TestStorageStaff');
INSERT INTO Storage_User_Staff(storage_identification, system_user_id) VALUES (2,2);

-- CONSTRAINT fkShipmentContainerNum -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination) VALUES (1,1,2);
-- CONSTRAINT fkShipmentStorageIdentificationDestination -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (1,1,1,'TestStorage','TestContinent','TestCountry',91.0,181.0);
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (1,1,1,0,'TST1',9999999,9999999,999999,999.9,1);
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination) VALUES (1,1,2);

-- CONSTRAINT fkCargoManifestTruckId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO CargoManifest(truck_id,ship_mmsi,loading_flag) VALUES (2,NULL,1);
-- CONSTRAINT fkCargoManifestShipMmsi -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO CargoManifest(truck_id,ship_mmsi,loading_flag) VALUES (NULL,123456789,1);

-- CONSTRAINT fkSystemUserRoleId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO SystemUser(role_id, name, email, password) VALUES (3,'TestSystemUser','testuser@email.com','pwd');

-- CONSTRAINT fkSystemUserShipmentShipmentId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO SystemUser_Shipment(system_user_id, shipment_id) VALUES (3, 1);
-- CONSTRAINT fkSystemUserShipmentSystemUserId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (2,1,1,'TestStorage2','TestContinent','TestCountry',90.0,180.0);
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination) VALUES (1,1,2);
INSERT INTO SystemUser_Shipment(system_user_id, shipment_id) VALUES (3, 1);

-- CONSTRAINT fkSystemUserFleetFleetId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO SystemUser_Fleet(system_user_id, fleet_id) VALUES (3,1);
-- CONSTRAINT fkSystemUserFleetSystemUserId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Fleet(id) VALUES (1);
INSERT INTO SystemUser_Fleet(system_user_id, fleet_id) VALUES (4,1);

-- CONSTRAINT fkShipSystemUserIdChiefElectricalEngineer -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Ship(mmsi,fleet_id,system_user_id_captain,system_user_id_chief_electrical_engineer,name,imo,num_generator,gen_power,callsign,vessel_type,ship_length,ship_width,capacity,draft)
VALUES (123456789,2,4,5,'TestShip',9999999,0,0,99999999,1,999.99,999.99,999.99,999.99);
-- CONSTRAINT fkShipFleedId -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO Ship(mmsi,fleet_id,system_user_id_captain,system_user_id_chief_electrical_engineer,name,imo,num_generator,gen_power,callsign,vessel_type,ship_length,ship_width,capacity,draft)
VALUES (123456789,2,3,3,'TestShip',9999999,0,0,99999999,1,999.99,999.99,999.99,999.99);

-- CONSTRAINT fkDynamicDataShipMmsi -- expected result FAIL (restrição de integridade violada - chave pai não encontrada)
INSERT INTO DynamicData(ship_mmsi,base_date_time,latitude,longitude,sog,cog,heading,position,transceiver_class)
VALUES (999999999,CURRENT_TIMESTAMP,91,181,0.0,359.0,511.0,NULL,'A');


-- DOMAIN RESTRICTIONS
-- Tests if all the columns in the database are restricted to a particular domain.

-- CONSTRAINT nnSystemUserIdManager -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,NULL,1,'TestStorageNULL','TestContinent','TestCountry',0.0,0.0);
-- CONSTRAINT nnStorageTypeId -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,NULL,'TestStorageNULL','TestContinent','TestCountry',0.0,0.0);
-- CONSTRAINT nnStorageName -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,NULL,'TestContinent','TestCountry',0.0,0.0);
-- CONSTRAINT nnContinent -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL',NULL,'TestCountry',0.0,0.0);
-- CONSTRAINT nnCountry -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL','TestContinent',NULL,0.0,0.0);
-- CONSTRAINT nnStorageLatitude -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL','TestContinent','TestCountry',NULL,0.0);
-- CONSTRAINT ckStorageLatitude -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL','TestContinent','TestCountry',92.0,0.0);
-- CONSTRAINT nnStorageLongitude -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL','TestContinent','TestCountry',0.0,NULL);
-- CONSTRAINT ckStorageLongitude -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (3,1,1,'TestStorageNULL','TestContinent','TestCountry',0.0,182.0);

-- CONSTRAINT nnContainerStorageIdentification -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,NULL,1,0,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnContainerCscPlateSerialNumber -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,NULL,0,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnCheckDigit -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,NULL,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnIsoCode -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,NULL,9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnGrossWeight -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',NULL,9999999,999999,999.9,1);
-- CONSTRAINT ckGrossWeight -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',-9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnTareWeight -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,NULL,999999,999.9,1);
-- CONSTRAINT ckTareWeight -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,-9999999,999999,999.9,1);
-- CONSTRAINT nnPayload -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,NULL,999.9,1);
-- CONSTRAINT ckPayload -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,-999999,999.9,1);
-- CONSTRAINT nnMaxVolume -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,999999,NULL,1);
-- CONSTRAINT ckMaxVolume -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,999999,-999.9,1);
-- CONSTRAINT nnRefrigeratedFlag -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,999999,999.9,NULL);
-- CONSTRAINT ckRefrigerated -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,1,0,'TST1',9999999,9999999,999999,999.9,2);

-- CONSTRAINT nnRules -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,NULL,'TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnModel -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235',NULL,'TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnManufacturerName -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235',NULL,'TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnOwnerName -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer',NULL,'TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnOwnerAddress -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName',NULL,'TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnFurnigation -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress',NULL,'TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnApprovalNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',NULL,'TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnAcepNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235',NULL,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnDateManufactured -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',NULL,99999,999999,99999);
-- CONSTRAINT nnMaxGrossMass -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),NULL,999999,99999);
-- CONSTRAINT nnStackingWeight -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,NULL,99999);
-- CONSTRAINT nnRackingTest -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,NULL);


-- IDENTITY RESTRICTIONS
-- Tests if the primary key in every table is unique and not null.

-- CONSTRAINT pkStorageIdentification -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (NULL,1,1,'TestStorageNULL','TestContinent','TestCountry',0.0,0.0);

-- CONSTRAINT pkContainerNum -- expected result FAIL
INSERT INTO Container(num,storage_identification,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (NULL,1,1,0,'TST1',9999999,9999999,999999,999.9,1);

-- CONSTRAINT pkCscPlateSerialNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (NULL,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);


-- APPLICATION RESTRICTIONS
-- Tests other sets of rules defined by the business model.

-- CONSTRAINT ckStorageLocation -- expected result FAIL
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (4,1,1,'TestStorage','TestContinent','TestCountry',0.0,0.0);
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude)
VALUES (5,1,1,'TestStorage','TestContinent','TestCountry',0.0,0.0);

-- CONSTRAINT unApprovalNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (3,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1236',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);

-- CONSTRAINT unAcepNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (3,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1236','TST1236',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,data_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (4,'TST1237','TST1237','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1237','TST1236',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);

-- CONSTRAINT unEmail -- expected result FAIL
INSERT INTO SystemUser(role_id, name, email, password) VALUES (1, 'TestUserEmail', 'testuseremail@email.com', 'pwd');
INSERT INTO SystemUser(role_id, name, email, password) VALUES (1, 'TestUserEmailDuplicate', 'testuseremail@email.com', 'pwd');
-- CONSTRAINT ckEmail -- expected result FAIL
INSERT INTO SystemUser(role_id, name, email, password) VALUES (1, 'TestUserEmail', 'fake-email.com', 'pwd');

-- CONSTRAINT ckStorageOriginDestination -- expected result FAIL
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination) VALUES (1,1,1);