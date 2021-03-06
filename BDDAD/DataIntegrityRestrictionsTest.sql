-- REFERENTIAL RESTRICTIONS
-- Tests if the foreign key refers to a primary key value of some table in the database.

-- CONSTRAINT FKSYSTEMUSERROLEID -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO SYSTEMUSER (REGISTRATION_CODE, NAME, EMAIL)
VALUES ('1', 'TestUser', 'test@email.com');
INSERT INTO ROLE(ID, NAME)
VALUES (4, 'Port Manager');
INSERT INTO EMPLOYEE(SYSTEM_USER_CODE_EMPLOYEE, ROLE_ID)
VALUES ('1', 4);

-- CONSTRAINT fkCountryName -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (1,1,'TestStorage','TestCountry',91.0,181.0,500.0,1);

-- CONSTRAINT fkStorageTypeId -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO COUNTRY (COUNTRY, CONTINENT, CAPITAL, ALPHA2, ALPHA3, POPULATION, LATITUDE, LONGITUDE)
VALUES ('TestCountry', 'TestContinent', 'TestCapital', '00', '00', 100.0, 10.1, 180.1);
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume, SYSTEM_USER_CODE_MANAGER)
VALUES (1,1,'TestStorage','TestCountry',91.0,181.0,500.0,1);

-- CONSTRAINT fkContainerCscPlateSerialNumber -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (1,1,0,'TST1',9999999,9999999,999999,999.9,1);

-- CONSTRAINT fkCscPlateSerialNumber -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO CscPlate_certificate(csc_plate_serial_number, certificate_id)
VALUES(1,1);

INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (1,'TST1234','TST1234','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',1001,1001,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);

-- CONSTRAINT fkCertificateID -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO CscPlate_certificate(csc_plate_serial_number, certificate_id)
VALUES(1,1);

-- CONSTRAINT fkContainerCargoManifestContainerNum -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Container_CargoManifest(container_num,partial_cargo_manifest_id,container_position_x,container_position_y,container_position_z) VALUES(2,1,0,0,0);

-- CONSTRAINT FKSHIPSYSTEMUSER -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Ship(mmsi,fleet_id,name,imo,num_generator,gen_power,callsign,vessel_type_id,ship_length,ship_width,capacity,draft, system_user_code_captain)
VALUES (123456789,2,'TestShip',9999999,0,0,99999999,1,999.99,999.99,999.99,999.99, '1');

-- CONSTRAINT fkDynamicDataShipMmsi -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO DynamicData(ship_mmsi,base_date_time,latitude,longitude,sog,cog,heading,position,transceiver_class)
VALUES (999999999,CURRENT_TIMESTAMP,91,181,0.0,359.0,511.0,NULL,'A');

-- CONSTRAINT FKCONTAINERSTORAGEIDENTIFICATION -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO CargoManifest_Partial(ship_mmsi, storage_identification, loading_flag, finishing_date_time) VALUES (1,1,1,CURRENT_TIMESTAMP);

-- CONSTRAINT fkContainerCargoManifestCargoManifestId -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Container_CargoManifest(container_num,partial_cargo_manifest_id,container_position_x,container_position_y,container_position_z) VALUES(1,1,0,0,0);

-- CONSTRAINT FKSHIPMENTSYSTEMUSER -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination,SYSTEM_USER_CODE_CLIENT) VALUES (2,1,2,'1');

-- CONSTRAINT fkShipmentStorageIdentificationDestination -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO SYSTEMUSER (REGISTRATION_CODE, NAME, EMAIL) VALUES ('2', 'TestUser2', 'test2@email.com');
INSERT INTO CLIENT(SYSTEM_USER_CODE_CLIENT) VALUES (2);
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination,SYSTEM_USER_CODE_CLIENT) VALUES (1,2,1,(select SYSTEM_USER_CODE_CLIENT from CLIENT));

-- CONSTRAINT FKSTORAGEDESTINATION -- expected result FAIL (restri��o de integridade violada - chave pai n�o encontrada)
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(123456789, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + 1, 'finished');


-- DOMAIN RESTRICTIONS
-- Tests if all the columns in the database are restricted to a particular domain.

-- CONSTRAINT nnStorageTypeName -- expected result FAIL
INSERT INTO StorageType(name)
VALUES (null);

-- CONSTRAINT nnStorageTypeId -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume, SYSTEM_USER_CODE_MANAGER)
VALUES (3,NULL,'TestStorageNULL','TestContinent',0.0,0.0,0.0,1);
-- CONSTRAINT nnStorageName -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,NULL,'TestContinent',0.0,0.0,0.0,1);
-- CONSTRAINT nnCountry -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,'TestStorageNULL',NULL,0.0,0.0,0.0,1);
-- CONSTRAINT nnStorageLatitude -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,'TestStorageNULL','TestContinent',NULL,0.0,0.0,1);
-- CONSTRAINT ckStorageLatitude -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,  country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,'TestStorageNULL','TestCountry',92.0,0.0,0.0,1);
-- CONSTRAINT nnStorageLongitude -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,  country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,'TestStorageNULL','TestCountry',0.0,NULL,0.0,1);
-- CONSTRAINT ckStorageLongitude -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,  country_name,latitude,longitude,max_volume,SYSTEM_USER_CODE_MANAGER)
VALUES (3,1,'TestStorageNULL','TestCountry',0.0,182.0,0.0,1);

-- CONSTRAINT nnContainerCscPlateSerialNumber -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,NULL,0,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnCheckDigit -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,NULL,'TST1',9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnIsoCode -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,NULL,9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnGrossWeight -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',NULL,9999999,999999,999.9,1);
-- CONSTRAINT ckGrossWeight -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',-9999999,9999999,999999,999.9,1);
-- CONSTRAINT nnTareWeight -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,NULL,999999,999.9,1);
-- CONSTRAINT ckTareWeight -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,-9999999,999999,999.9,1);
-- CONSTRAINT nnPayload -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,NULL,999.9,1);
-- CONSTRAINT ckPayload -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,-999999,999.9,1);
-- CONSTRAINT nnMaxVolume -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,999999,NULL,1);
-- CONSTRAINT ckMaxVolume -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,999999,-999.9,1);
-- CONSTRAINT nnRefrigeratedFlag -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,999999,999.9,NULL);
-- CONSTRAINT ckRefrigerated -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (2,1,0,'TST1',9999999,9999999,999999,999.9,2);

-- CONSTRAINT nnRules -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,NULL,'TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnModel -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235',NULL,'TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnManufacturerName -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235',NULL,'TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnOwnerName -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer',NULL,'TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnOwnerAddress -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName',NULL,'TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnFurnigation -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress',NULL,'TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnApprovalNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',NULL,'TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnAcepNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235',NULL,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
-- CONSTRAINT nnDateManufactured -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',NULL,99999,999999,99999);
-- CONSTRAINT nnMaxGrossMass -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),NULL,999999,99999);
-- CONSTRAINT nnStackingWeight -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,NULL,99999);
-- CONSTRAINT nnRackingTest -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (2,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,NULL);

-- CONSTRAINT nnCertificateName -- expected result FAIL
INSERT INTO Certificate(id, name)
VALUES (3, NULL);

-- CONSTRAINT nnCscPlateSerialNumber -- expected result FAIL
INSERT INTO CscPlate_Certificate(csc_plate_serial_number, certificate_id)
VALUES (NULL, 3);


-- IDENTITY RESTRICTIONS
-- Tests if the primary key in every table is unique and not null.

-- CONSTRAINT pkContainerNum -- expected result FAIL
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (NULL,1,0,'TST1',9999999,9999999,999999,999.9,1);
INSERT INTO Container(num,csc_plate_serial_number,check_digit,iso_code,gross_weight,tare_weight,payload,max_volume,refrigerated_flag)
VALUES (1,1,0,'TST1',9999999,9999999,999999,999.9,1);

-- CONSTRAINT pkStorageIdentification -- expected result FAIL
INSERT INTO Storage(identification,storage_type_id,name,country_name,MAX_VOLUME,latitude,longitude,SYSTEM_USER_CODE_MANAGER)
VALUES (NULL,1,'TestStorageNULL','TestCountry',10.0,0.0,0.0,1);

-- CONSTRAINT pkCscPlateSerialNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (NULL,'TST1235','TST1235','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails','TST1235','TST1235',TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);


-- APPLICATION RESTRICTIONS
-- Tests other sets of rules defined by the business model.

-- CONSTRAINT unApprovalNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (3,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',1002,1003,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (4,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',1002,1002,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);

-- CONSTRAINT unAcepNumber -- expected result FAIL
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (5,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',1003,1002,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);
INSERT INTO CscPlate(serial_number,rules,model,manufacturer_name,owner_name,owner_address,furnigation,approval_number,acep_number,date_manufactured,max_gross_mass,stacking_weight,racking_test)
VALUES (6,'TST1236','TST1236','TestManufacturer','TestOwnerName','TestAddress','TestFurnigationDetails',1004,1002,TO_DATE('01/01/2000','DD/MM/YYYY'),99999,999999,99999);

-- CONSTRAINT ckStorageOriginDestination -- expected result FAIL
INSERT INTO Shipment(container_num,storage_identification_origin,storage_identification_destination,SYSTEM_USER_CODE_CLIENT) VALUES (1,1,1,(SELECT REGISTRATION_CODE FROM SYSTEMUSER));

--CONSTRAINT setStatus -- expected result FAIL
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 2, 4, TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-05-21 17:48:36', 'YYYY-MM-DD HH24:MI:SS'), 'N/A');
--CONSTRAINT ckTripDestination -- expected result FAIL
INSERT INTO ShipTrip(ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date, arrival_date, status)
VALUES(100000001, 2, 4, TO_TIMESTAMP('2020-05-25 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2020-05-20 17:48:36', 'YYYY-MM-DD HH24:MI:SS'), 'N/A');