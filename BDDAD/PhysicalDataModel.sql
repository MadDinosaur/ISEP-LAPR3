-- drop existing tables to avoid naming conflicts
DROP TABLE StorageType CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage CASCADE CONSTRAINTS PURGE;
DROP TABLE Container CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate CASCADE CONSTRAINTS PURGE;
DROP TABLE Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate_Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE Container_CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest_Partial CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest_Full CASCADE CONSTRAINTS PURGE;
DROP TABLE Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE VesselType CASCADE CONSTRAINTS PURGE;
DROP TABLE Ship CASCADE CONSTRAINTS PURGE;
DROP TABLE DynamicData CASCADE CONSTRAINTS PURGE;
DROP TABLE ShipTrip CASCADE CONSTRAINTS PURGE;
DROP TABLE Country CASCADE CONSTRAINTS PURGE;
DROP TABLE Border CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage_Path CASCADE CONSTRAINTS PURGE;
DROP TABLE Role CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser CASCADE CONSTRAINTS PURGE;
DROP TABLE Truck CASCADE CONSTRAINTS PURGE;

-- create tables
CREATE TABLE Role
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkRoleId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnRoleName NOT NULL
        CONSTRAINT unRoleName UNIQUE
);

CREATE TABLE SystemUser
(
    registration_code VARCHAR(10)
        CONSTRAINT pkSystemUserCode PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnSystemUserName NOT NULL,
    email VARCHAR(40)
        CONSTRAINT nnSystemUserEmail NOT NULL
        CONSTRAINT unSystemUserEmail UNIQUE
        CONSTRAINT ckSystemUserEmail CHECK (email LIKE '%_@__%.__%'),
    role_id INTEGER
        CONSTRAINT nnSystemUserRoleId NOT NULL
);

CREATE TABLE StorageType
(
    id   INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkStorageTypeId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnStorageTypeName NOT NULL
        CONSTRAINT unStorageTypeName UNIQUE
);

CREATE TABLE Storage
(
    identification         INTEGER
        CONSTRAINT pkStorageIdentification PRIMARY KEY,
    storage_type_id        INTEGER
        CONSTRAINT nnStorageTypeId NOT NULL,
    country_name           VARCHAR(30)
        CONSTRAINT nnCountry NOT NULL,
    name                   VARCHAR(20)
        CONSTRAINT nnStorageName NOT NULL,
    max_volume            NUMBER(5,2)
        CONSTRAINT nnStorageMaxVolume NOT NULL,
        CONSTRAINT ckStorageMaxCapacity CHECK (max_volume >= 0),
    latitude               NUMBER(7, 5)
        CONSTRAINT nnStorageLatitude NOT NULL,
        CONSTRAINT ckStorageLatitude CHECK (latitude BETWEEN -90 AND 90 OR latitude = 91),
    longitude              NUMBER(8, 5)
        CONSTRAINT nnStorageLongitude NOT NULL,
        CONSTRAINT ckStorageLongitude CHECK (longitude BETWEEN -180 AND 180 or longitude = 181)

);

CREATE TABLE Container
(
    num                     INTEGER
        CONSTRAINT pkContainerNum PRIMARY KEY,
    csc_plate_serial_number INTEGER
        CONSTRAINT nnContainerCscPlateSerialNumber NOT NULL,
    check_digit             NUMBER(1)
        CONSTRAINT nnCheckDigit NOT NULL,
    iso_code                VARCHAR(4)
        CONSTRAINT nnIsoCode NOT NULL,
    gross_weight            NUMBER(7)
        CONSTRAINT nnGrossWeight NOT NULL,
    CONSTRAINT ckGrossWeight CHECK (gross_weight >= 0),
    tare_weight             NUMBER(7)
        CONSTRAINT nnTareWeight NOT NULL,
    CONSTRAINT ckTareWeight CHECK (tare_weight >= 0),
    payload                 NUMBER(7)
        CONSTRAINT nnPayload NOT NULL,
    CONSTRAINT ckPayload CHECK (payload >= 0),
    max_volume              NUMBER(4, 1)
        CONSTRAINT nnMaxVolume NOT NULL,
    CONSTRAINT ckMaxVolume CHECK (max_volume >= 0),
    refrigerated_flag       NUMBER(1)
        CONSTRAINT nnRefrigeratedFlag NOT NULL,
    CONSTRAINT ckRefrigerated CHECK (refrigerated_flag BETWEEN 0 AND 1)
);

CREATE TABLE CscPlate
(
    serial_number     INTEGER
        CONSTRAINT pkCscPlateSerialNumber PRIMARY KEY,
    rules             VARCHAR(20)
        CONSTRAINT nnRules NOT NULL,
    model             VARCHAR(10)
        CONSTRAINT nnModel NOT NULL,
    manufacturer_name VARCHAR(50)
        CONSTRAINT nnManufacturerName NOT NULL,
    owner_name        VARCHAR(50)
        CONSTRAINT nnOwnerName NOT NULL,
    owner_address     VARCHAR(50)
        CONSTRAINT nnOwnerAddress NOT NULL,
    furnigation       VARCHAR(50)
        CONSTRAINT nnFurnigation NOT NULL,
    approval_number   INTEGER
        CONSTRAINT nnApprovalNumber NOT NULL
        CONSTRAINT unApprovalNumber UNIQUE,
    acep_number       INTEGER
        CONSTRAINT nnAcepNumber NOT NULL
        CONSTRAINT unAcepNumber UNIQUE,
    date_manufactured DATE
        CONSTRAINT nnDateManufactured NOT NULL,
    max_gross_mass    NUMBER(5)
        CONSTRAINT nnMaxGrossMass NOT NULL,
    stacking_weight   NUMBER(6)
        CONSTRAINT nnStackingWeight NOT NULL,
    racking_test      NUMBER(5)
        CONSTRAINT nnRackingTest NOT NULL
);

CREATE TABLE Certificate
(
    id   INTEGER
        CONSTRAINT pkCertificateId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnCertificateName NOT NULL
);

CREATE TABLE CscPlate_Certificate
(
    csc_plate_serial_number INTEGER
        CONSTRAINT nnCscPlateSerialNumber NOT NULL,
    certificate_id          INTEGER
        CONSTRAINT nnCertificateId NOT NULL
);

CREATE TABLE Container_CargoManifest
(
    id INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkContainer_CargoManifest PRIMARY KEY,
    container_num        INTEGER
        CONSTRAINT nnContainerNum NOT NULL,
    full_cargo_manifest_id    INTEGER
        DEFAULT NULL,
    partial_cargo_manifest_id INTEGER
        DEFAULT NULL,
        -- make sure each row only has one type of cargo manifest
        CONSTRAINT ckCargoManifestId CHECK (
            (full_cargo_manifest_id IS NOT NULL AND partial_cargo_manifest_id IS NULL) OR
            (full_cargo_manifest_id IS NULL AND partial_cargo_manifest_id IS NOT NULL)),
        -- make sure each cargo manifest - container combination is unique
        CONSTRAINT unCargoManifestContainer UNIQUE (container_num, full_cargo_manifest_id, partial_cargo_manifest_id),
    container_position_x INTEGER
        CONSTRAINT nnContainerPositionX NOT NULL,
    container_position_y INTEGER
        CONSTRAINT nnContainerPositionY NOT NULL,
    container_position_z INTEGER
        CONSTRAINT nnContainerPositionZ NOT NULL,
        -- make sure containers are not in the same position on a cargo manifest
        CONSTRAINT unContainerPosition UNIQUE (full_cargo_manifest_id, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
);

CREATE TABLE Shipment
(
    id                                 INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkShipmentId PRIMARY KEY,
    container_num                      INTEGER
        CONSTRAINT nnShipmentContainerNum NOT NULL,
    storage_identification_origin      INTEGER
        CONSTRAINT nnStorageIdentificationOrigin NOT NULL,
    storage_identification_destination INTEGER
        CONSTRAINT nnStorageIdentificationDestination NOT NULL,
    CONSTRAINT ckStorageOriginDestination CHECK (storage_identification_origin != storage_identification_destination),
    parting_date TIMESTAMP,
    arrival_date TIMESTAMP,
    system_user_code_client VARCHAR(5)
        CONSTRAINT nnSystemUserCodeClient NOT NULL
);

CREATE TABLE CargoManifest_Partial
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkCargoManifestId PRIMARY KEY,
    truck_id     INTEGER
        DEFAULT NULL,
    ship_mmsi    NUMBER(9)
        DEFAULT NULL,
        CONSTRAINT ckTransportation CHECK (truck_id IS NULL OR ship_mmsi IS NULL),
    storage_identification INTEGER
        CONSTRAINT nnCargoStorageIdentification NOT NULL,
    loading_flag NUMBER(1)
        CONSTRAINT nnCargoLoadingFlag NOT NULL
        CONSTRAINT ckLoadingFlag CHECK (loading_flag BETWEEN 0 AND 1),
    finishing_date_time    TIMESTAMP
        DEFAULT NULL,
    status VARCHAR(20)
        DEFAULT 'pending'
        CONSTRAINT nnCargoStatus NOT NULL
        CONSTRAINT setCargoStatus CHECK (status IN ('pending', 'finished'))
);

CREATE TABLE CargoManifest_Full
(
     id           INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkFullCargoManifestId PRIMARY KEY,
    ship_mmsi    NUMBER(9)
        DEFAULT NULL,
    storage_identification INTEGER
        DEFAULT NULL,
        -- make sure each row only has one type of manifest (ship or storage)
        CONSTRAINT ckCargoManifestType CHECK (
            (ship_mmsi IS NOT NULL AND storage_identification IS NULL) OR
            (ship_mmsi IS NULL AND storage_identification IS NOT NULL)),
    finishing_date_time    TIMESTAMP
        DEFAULT NULL,
    status VARCHAR(20)
        DEFAULT 'pending'
        CONSTRAINT setFullCargoStatus CHECK (status IS NULL OR status IN ('pending', 'finished'))
);

CREATE TABLE Truck
(
    id INTEGER CONSTRAINT pkTruckId PRIMARY KEY
);

CREATE TABLE Fleet
(
    id INTEGER CONSTRAINT pkFleetId PRIMARY KEY
);

CREATE TABLE VesselType
(
    id INTEGER
    CONSTRAINT pkVesselType PRIMARY KEY
);

CREATE TABLE Ship
(
    mmsi                                     NUMBER(9)
        CONSTRAINT pkShipMMSI PRIMARY KEY
        CONSTRAINT ckMMSI CHECK (mmsi BETWEEN 100000000 AND 999999999),
    fleet_id                                 INTEGER
        CONSTRAINT nnShipFleetId NOT NULL,
    name                                     VARCHAR(20)
        CONSTRAINT nnShipName NOT NULL,
    imo                                      NUMBER(7)
        CONSTRAINT nnImo NOT NULL
        CONSTRAINT unImo UNIQUE
        CONSTRAINT ckIMO CHECK (imo BETWEEN 1000000 AND 9999999),
    num_generator                            NUMBER(3)
        CONSTRAINT nnNumGenerator NOT NULL
        CONSTRAINT ckNumGenerator CHECK (num_generator >= 0),
    gen_power                                NUMBER(5, 2)
        CONSTRAINT nnGenPower NOT NULL
        CONSTRAINT ckGenPower CHECK (gen_power >= 0),
    callsign                                 VARCHAR(8)
        CONSTRAINT nnCallsign NOT NULL
        CONSTRAINT unCallsign UNIQUE,
    vessel_type_id                           NUMBER(2)
        CONSTRAINT nnVesselType NOT NULL,
    ship_length                              NUMBER(5, 2)
        CONSTRAINT nnShipLength NOT NULL
        CONSTRAINT ckShipLength CHECK (ship_length >= 0),
    ship_width                               NUMBER(5, 2)
        CONSTRAINT nnShipWidth NOT NULL
        CONSTRAINT ckShipWidth CHECK (ship_width >= 0),
    capacity                                 NUMBER(5, 2)
        CONSTRAINT nnCapacity NOT NULL
        CONSTRAINT ckCapacity CHECK (capacity >= 0),
    draft                                    NUMBER(5, 2)
        CONSTRAINT nnDraft NOT NULL
        CONSTRAINT ckDraft CHECK (draft >= 0),
    system_user_code_captain                              VARCHAR(10)
        CONSTRAINT nnCaptainID NOT NULL
        CONSTRAINT unCaptainID UNIQUE
);

CREATE TABLE DynamicData
(
    ship_mmsi         NUMBER(9)
        CONSTRAINT nnDataShipMmsi NOT NULL,
    base_date_time    TIMESTAMP
        CONSTRAINT nnBaseDateTime NOT NULL,
    latitude          NUMBER(7, 5)
        CONSTRAINT nnLatitute NOT NULL
        CONSTRAINT ckLatitude CHECK (latitude BETWEEN -90 AND 91),
    longitude         NUMBER(8, 5)
        CONSTRAINT nnLongitude NOT NULL
        CONSTRAINT ckLongitude CHECK (longitude BETWEEN -180 AND 181),
    sog               NUMBER(5, 2)
        CONSTRAINT nnSOG NOT NULL
        CONSTRAINT ckSOG CHECK (sog >= 0),
    cog               NUMBER(5, 2)
        CONSTRAINT nnCOG NOT NULL
        CONSTRAINT ckCOG CHECK (cog BETWEEN 0 AND 359),
    heading           NUMBER(5, 2)
        CONSTRAINT nnHeading NOT NULL
        CONSTRAINT ckHeading CHECK (heading BETWEEN 0 AND 359 OR heading = 511),
    position          INTEGER,
    transceiver_class VARCHAR(1)
        CONSTRAINT nnTransceiverClass NOT NULL
);

CREATE TABLE ShipTrip
(
    ship_mmsi         NUMBER(9)
        CONSTRAINT nnShipMmsi NOT NULL,
    storage_identification_origin      INTEGER
        CONSTRAINT nnTripIdentificationOrigin NOT NULL,
    storage_identification_destination INTEGER
        CONSTRAINT nnTripIdentificationDestination NOT NULL,
    parting_date                       TIMESTAMP
        CONSTRAINT nnPartingDate NOT NULL,
    arrival_date                       TIMESTAMP
        CONSTRAINT nnArrivalDate NOT NULL,
    status VARCHAR(20)
        CONSTRAINT nnStatus NOT NULL
        CONSTRAINT setStatus CHECK (status IN ('in progress', 'not started', 'finished')),
    CONSTRAINT ckTripDestination CHECK (parting_date < arrival_date)
);

CREATE TABLE Country
(
    country         VARCHAR(30)
        CONSTRAINT pkCountryName PRIMARY KEY,
    continent       VARCHAR(20)
        CONSTRAINT nnCountryContinent NOT NULL,
    capital         VARCHAR(30)
        CONSTRAINT nnCountryCapital NOT NULL,
    alpha2          VARCHAR(2)
        CONSTRAINT nnCountryAlpha2 NOT NULL,
    alpha3          VARCHAR(3)
        CONSTRAINT nnCountryAlpha3 NOT NULL,
    population      NUMBER(6,2)
        CONSTRAINT nnCountryPopulation NOT NULL,
    latitude          NUMBER(7, 5)
        CONSTRAINT nnCapitalLatitude NOT NULL
        CONSTRAINT ckCapitalLatitude CHECK (latitude BETWEEN -90 AND 91),
    longitude         NUMBER(8, 5)
        CONSTRAINT nnCapitalLongitude NOT NULL
        CONSTRAINT ckCapitalLongitude CHECK (longitude BETWEEN -180 AND 181)
);

CREATE TABLE Border
(
    country_name       VARCHAR(30)
        CONSTRAINT nnBorderContinent NOT NULL,
    country_border     VARCHAR(30)
        CONSTRAINT nnBorderingContinent NOT NULL,
    CONSTRAINT ckBorderValidity CHECK (country_name != country_border)
);

CREATE TABLE Storage_Path
(
    storage_id1         INTEGER
        CONSTRAINT nnPathStorageId1 NOT NULL,
    storage_id2        INTEGER
        CONSTRAINT nnPathStorageId2 NOT NULL,
    distance           INTEGER
        CONSTRAINT nnPathDistance NOT NULL,
    CONSTRAINT ckPathValidity CHECK (storage_id1 != storage_id2)
);

-- define foreign keys and combined primary keys
ALTER TABLE SystemUser
    ADD CONSTRAINT fkSystemUserRoleId FOREIGN KEY (role_id) REFERENCES Role (id);

ALTER TABLE Storage
    ADD CONSTRAINT fkStorageTypeId FOREIGN KEY (storage_type_id) REFERENCES StorageType (id)
    ADD CONSTRAINT fkCountryName FOREIGN KEY (country_name) REFERENCES Country (country);

ALTER TABLE Container
    ADD CONSTRAINT fkContainerCscPlateSerialNumber FOREIGN KEY (csc_plate_serial_number) REFERENCES CscPlate (serial_number);

ALTER TABLE Container_CargoManifest
    ADD CONSTRAINT fkContainerCargoManifestContainerNum FOREIGN KEY (container_num) REFERENCES Container (num)
    ADD CONSTRAINT fkContainerFullCargoManifestCargoManifestId FOREIGN KEY (full_cargo_manifest_id) REFERENCES CargoManifest_Full (id)
    ADD CONSTRAINT fkContainerPartialCargoManifestCargoManifestId FOREIGN KEY (partial_cargo_manifest_id) REFERENCES CargoManifest_Partial (id);

ALTER TABLE Shipment
    ADD CONSTRAINT fkShipmentStorageIdentificationOrigin FOREIGN KEY (storage_identification_origin) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentStorageIdentificationDestination FOREIGN KEY (storage_identification_destination) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentContainerNum FOREIGN KEY (container_num) REFERENCES Container (num)
    ADD CONSTRAINT fkShipmentSystemUser FOREIGN KEY (system_user_code_client) REFERENCES SystemUser (registration_code);

ALTER TABLE CargoManifest_Partial
    ADD CONSTRAINT fkContainerStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage(identification)
    ADD CONSTRAINT fkCargoManifestShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi)
    ADD CONSTRAINT fkCargoManifestTruckId FOREIGN KEY (truck_id) REFERENCES Truck (id);

ALTER TABLE CargoManifest_Full
    ADD CONSTRAINT fkFullCargoManifestShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);

ALTER TABLE Ship
    ADD CONSTRAINT fkShipFleetId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT fkShipVesselTypeId FOREIGN KEY (vessel_type_id) REFERENCES VesselType (id)
    ADD CONSTRAINT fkShipSystemUser FOREIGN KEY (system_user_code_captain) REFERENCES SystemUser (registration_code);

ALTER TABLE DynamicData
    ADD CONSTRAINT fkDynamicDataShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi)
    ADD CONSTRAINT pkDynamicData PRIMARY KEY (ship_mmsi, base_date_time);

ALTER TABLE CscPlate_Certificate
    ADD CONSTRAINT fkCscPlateSerialNumber FOREIGN KEY (csc_plate_serial_number) REFERENCES CscPlate (serial_number)
    ADD CONSTRAINT fkCertificateID FOREIGN KEY (certificate_id) REFERENCES Certificate (id)
    ADD CONSTRAINT pkCscPlateCertificate PRIMARY KEY (csc_plate_serial_number, certificate_id);

ALTER TABLE ShipTrip
    ADD CONSTRAINT fkShipMMSI FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi)
    ADD CONSTRAINT fkStorageOrigin FOREIGN KEY (storage_identification_origin) REFERENCES Storage (identification)
    ADD CONSTRAINT fkStorageDestination FOREIGN KEY (storage_identification_destination) REFERENCES Storage (identification)
    ADD CONSTRAINT pkShipTrip PRIMARY KEY (ship_mmsi, storage_identification_origin, storage_identification_destination, parting_date);

ALTER TABLE Border
    ADD CONSTRAINT fkBorderCountry1 FOREIGN KEY (country_name) REFERENCES Country (country)
    ADD CONSTRAINT fkBorderCountry2 FOREIGN KEY (country_border) REFERENCES Country (country)
    ADD CONSTRAINT pkBorder PRIMARY KEY (country_name, country_border);

ALTER TABLE Storage_Path
    ADD CONSTRAINT fkPathStorage1 FOREIGN KEY (storage_id1) REFERENCES Storage (identification)
    ADD CONSTRAINT fkPathStorage2 FOREIGN KEY (storage_id2) REFERENCES Storage (identification)
    ADD CONSTRAINT pkStoragePath PRIMARY KEY (storage_id1, storage_id2);

-- define database triggers
CREATE OR REPLACE TRIGGER trgUpdateCargoManifest
    BEFORE UPDATE ON CargoManifest_Partial
    FOR EACH ROW
    WHEN (((old.finishing_date_time IS NULL AND new.finishing_date_time IS NOT NULL)
        OR (new.finishing_date_time <> old.finishing_date_time)
        OR (new.status <> old.status)))
    DECLARE
        vShipCargoManifest_Full CargoManifest_Full%rowtype;
        vStorageCargoManifest_Full CargoManifest_Full%rowtype;
        vContainer Container_CargoManifest%rowtype;
    BEGIN
        IF :new.status LIKE 'finished' THEN
            -- find current full manifest for the storage
            BEGIN
                SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                WHERE storage_identification = :new.storage_identification
                AND FINISHING_DATE_TIME IS NULL;
            EXCEPTION
                -- if full manifest does not exist yet, create it
                WHEN no_data_found THEN
                     INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (:new.storage_identification, NULL);
                     SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                        WHERE storage_identification = :new.storage_identification
                        AND finishing_date_time IS NULL;
            END;
            IF :new.ship_mmsi IS NOT NULL THEN
            -- find current full manifest for the ship
            BEGIN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND FINISHING_DATE_TIME IS NULL;
            EXCEPTION
                -- if full manifest does not exist yet, create it
                WHEN no_data_found THEN
                     INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (:new.ship_mmsi, NULL);
                     SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                        WHERE ship_mmsi = :new.ship_mmsi
                        AND finishing_date_time IS NULL;
            END;
            END IF;

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    -- loading operation
                    IF :new.loading_flag = 1 THEN
                        -- add containers to ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                            VALUES (vContainer.container_num, vShipCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                        END IF;
                        -- remove containers from storage
                        DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id AND container_num = vContainer.container_num;

                    -- unloading operation
                    ELSE
                        -- remove containers from ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id AND container_num = vContainer.container_num;
                        END IF;
                        -- add containers to storage
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vStorageCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    END IF;
                END LOOP;

            -- close modified cargo manifest and create a new current one
            IF :new.ship_mmsi IS NOT NULL THEN
                UPDATE CargoManifest_Full SET finishing_date_time = :new.finishing_date_time, status = 'finished' WHERE id = vShipCargoManifest_Full.id;
                INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (vShipCargoManifest_Full.ship_mmsi, NULL);
                FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id)
                    LOOP
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = vShipCargoManifest_Full.ship_mmsi AND finishing_date_time IS NULL), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    END LOOP;
            END IF;

            UPDATE CargoManifest_Full SET finishing_date_time = :new.finishing_date_time, status = 'finished' WHERE id = vStorageCargoManifest_Full.id;
            INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (vStorageCargoManifest_Full.storage_identification, NULL);
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE storage_identification = vStorageCargoManifest_Full.storage_identification AND finishing_date_time IS NULL), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;
        ELSE
            -- find last estimate manifest for the ship
            IF :new.ship_mmsi IS NOT NULL THEN
            BEGIN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND status LIKE 'pending'
                AND finishing_date_time = (SELECT MAX(cf.finishing_date_time) FROM CargoManifest_Full cf WHERE cf.finishing_date_time < :new.finishing_date_time AND cf.ship_mmsi IS NOT NULL);
            EXCEPTION
                -- if estimate manifest does not exist, get current manifest
                WHEN no_data_found THEN
                    BEGIN
                         SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                         WHERE ship_mmsi = :new.ship_mmsi
                         AND FINISHING_DATE_TIME IS NULL;
                    EXCEPTION
                         WHEN NO_DATA_FOUND THEN
                             INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (:new.ship_mmsi, NULL);
                             SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                                WHERE ship_mmsi = :new.ship_mmsi
                                AND finishing_date_time IS NULL;
                    END;
            END;
            END IF;
            -- find last estimate manifest for the storage
            BEGIN
                SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                WHERE storage_identification = :new.storage_identification
                AND status LIKE 'pending'
                AND finishing_date_time = (SELECT MAX(cf.finishing_date_time) FROM CargoManifest_Full cf WHERE cf.finishing_date_time < :new.finishing_date_time AND cf.storage_identification IS NOT NULL);
            EXCEPTION
                -- if estimate manifest does not exist, get current manifest
                WHEN no_data_found THEN
                    BEGIN
                         SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                         WHERE storage_identification = :new.storage_identification
                         AND FINISHING_DATE_TIME IS NULL;
                    EXCEPTION
                         WHEN NO_DATA_FOUND THEN
                             INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (:new.storage_identification, NULL);
                             SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                                WHERE storage_identification = :new.storage_identification
                                AND finishing_date_time IS NULL;
                    END;
            END;

            -- create new cargo manifest as a copy
            IF :new.ship_mmsi IS NOT NULL THEN
                INSERT INTO CargoManifest_Full (ship_mmsi, finishing_date_time, status) VALUES (:new.ship_mmsi, :new.finishing_date_time, 'pending');
                FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending'), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;
            END IF;

            INSERT INTO CargoManifest_Full (storage_identification, finishing_date_time, status) VALUES (:new.storage_identification, :new.finishing_date_time, 'pending');
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE storage_identification = :new.storage_identification AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending'), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;

            -- update variable
            IF :new.ship_mmsi IS NOT NULL THEN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending';
            END IF;

            SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full WHERE storage_identification = :new.storage_identification AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending';

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    -- loading operation
                    IF :new.loading_flag = 1 THEN
                        -- add containers to ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                            VALUES (vContainer.container_num, vShipCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                        END IF;
                        -- remove containers from storage
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id AND container_num = vContainer.container_num;
                    -- unloading operation
                    ELSE
                        -- remove containers from ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id AND container_num = vContainer.container_num;
                        END IF;
                        -- add containers to storage
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vStorageCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    END IF;
                END LOOP;
        END IF;
    END;
/
ALTER TRIGGER trgUpdateCargoManifest ENABLE;
/

CREATE OR REPLACE TRIGGER trgUpdateShipmentDates
    AFTER UPDATE ON CargoManifest_Partial
    FOR EACH ROW
    WHEN (old.status LIKE 'pending' AND new.status LIKE 'finished')
    DECLARE
        -- list of containers in new cargo manifest
        CURSOR vContainerCargoManifests IS SELECT * FROM CONTAINER_CARGOMANIFEST WHERE PARTIAL_CARGO_MANIFEST_ID = :new.ID;
        -- individual container
        vContainerCargoManifest CONTAINER_CARGOMANIFEST%rowtype;
        -- shipment id
        vShipmentId SHIPMENT.id%type;
    BEGIN
        -- container being loaded
        IF :new.LOADING_FLAG = 1 THEN
            OPEN vContainerCargoManifests;
            LOOP
                FETCH vContainerCargoManifests INTO vContainerCargoManifest;
                EXIT WHEN vContainerCargoManifests%NOTFOUND;
                -- checks if containers have been loaded from origin storage
                SELECT ID INTO vShipmentId FROM SHIPMENT
                    WHERE SHIPMENT.CONTAINER_NUM = vContainerCargoManifest.CONTAINER_NUM
                    AND PARTING_DATE IS NULL
                    AND STORAGE_IDENTIFICATION_ORIGIN = :new.STORAGE_IDENTIFICATION;
                -- if no error is raised above, the load date is updated
                UPDATE SHIPMENT SET PARTING_DATE = :new.FINISHING_DATE_TIME WHERE ID = vShipmentId ;
            END LOOP;
            CLOSE vContainerCargoManifests;
        ELSE
            -- container being offloaded
            OPEN vContainerCargoManifests;
            LOOP
                FETCH vContainerCargoManifests INTO vContainerCargoManifest;
                EXIT WHEN vContainerCargoManifests%NOTFOUND;
                -- checks if containers have been offloaded into destination storage
                SELECT ID INTO vShipmentId FROM SHIPMENT
                    WHERE SHIPMENT.CONTAINER_NUM = vContainerCargoManifest.CONTAINER_NUM
                    AND PARTING_DATE IS NOT NULL
                    AND ARRIVAL_DATE IS NULL
                    AND STORAGE_IDENTIFICATION_DESTINATION = :new.STORAGE_IDENTIFICATION;
                -- if no error is raised above, the offload date is updated
                UPDATE SHIPMENT SET ARRIVAL_DATE = :new.FINISHING_DATE_TIME WHERE ID = vShipmentId ;
            END LOOP;
            CLOSE vContainerCargoManifests;
        END IF;
        DBMS_OUTPUT.PUT_LINE('Container no. ' || vContainerCargoManifest.container_num || ' shipment date updated.');
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN;
    END;
/
ALTER TRIGGER trgUpdateShipmentDates ENABLE;
/

DROP SEQUENCE user_sequence;
CREATE SEQUENCE user_sequence;

CREATE OR REPLACE TRIGGER user_gen_code
    BEFORE INSERT ON systemuser
    FOR EACH ROW
BEGIN
   SELECT TO_CHAR(user_sequence.nextval)
   INTO :new.registration_code
   FROM dual;
END;
/
ALTER TRIGGER user_gen_code ENABLE;
/

CREATE OR REPLACE TRIGGER tgrManifestInTransit
BEFORE INSERT ON cargomanifest_partial
FOR EACH ROW
DECLARE
    vShipmmsi cargomanifest_partial.ship_mmsi%type;
BEGIN
    SELECT ship_mmsi
    INTO vShipmmsi
        FROM shiptrip
        WHERE ship_mmsi = :new.ship_mmsi
        AND status = 'in progress';

    raise_application_error(-20000, 'Ship is in transit, unable to add container to cargo manifest.');

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
                dbms_output.put_line('Verified if the ship is in transit.');
END;
/
ALTER TRIGGER tgrManifestInTransit ENABLE;
/

CREATE OR REPLACE TRIGGER tgrValidateCapacity
    AFTER UPDATE ON CARGOMANIFEST_PARTIAL
    FOR EACH ROW
    WHEN (new.FINISHING_DATE_TIME IS NOT NULL AND new.SHIP_MMSI IS NOT NULL AND new.LOADING_FLAG = 1)

    DECLARE
        varOccupancyNumber number;
        varManifestId CARGOMANIFEST_FULL.ID%type;

        varShipCapacity SHIP.CAPACITY%type;
        varContainerVolume CONTAINER.MAX_VOLUME%type;

        ex_error EXCEPTION;

    BEGIN
        -- get full cargo manifest id, estimated for the partial manifest's date
        BEGIN
            SELECT c.id
            INTO varManifestId
            FROM cargomanifest_full c
            WHERE c.finishing_date_time = (SELECT MAX(cf.finishing_date_time)
                                           FROM cargomanifest_full cf
                                           WHERE cf.finishing_date_time <= :new.finishing_date_time
                                           AND cf.ship_mmsi = :new.ship_mmsi)
            AND c.ship_mmsi = :new.ship_mmsi
            AND c.status NOT LIKE 'finished';
        EXCEPTION
            -- if there are no estimated full cargo manifests, get the current one
            WHEN NO_DATA_FOUND THEN
                SELECT c.id
                INTO varManifestId
                FROM cargomanifest_full c
                WHERE c.finishing_date_time IS NULL
                AND c.ship_mmsi = :new.ship_mmsi
                AND c.status IS NULL;
        END;

        -- get container volumes and ship capacity
        SELECT (s.capacity), sum(con.max_volume)
        INTO varShipCapacity, varContainerVolume
        FROM cargomanifest_full c, container_cargoManifest cc, ship s, container con
        WHERE s.mmsi = :new.ship_mmsi
        AND c.id = varManifestId
        AND c.ship_mmsi = s.mmsi
        AND cc.full_cargo_manifest_id = c.id
        AND cc.container_num = con.num
        GROUP BY s.capacity;

        varOccupancyNumber := varContainerVolume/varShipCapacity;

        -- check if update causes overloading and reverse the operation if it does
         IF (varOccupancyNumber > 1) THEN
            RAISE EX_ERROR;
        END IF;

     EXCEPTION
        WHEN EX_ERROR THEN
            RAISE_APPLICATION_ERROR(-20014,'Containers in cargo manifest exceed ship capacity. Please remove containers from manifest or issue an unloading order.');
        WHEN NO_DATA_FOUND THEN
            varOccupancyNumber := 0;

    END tgrValidateCapacity;
/
ALTER TRIGGER tgrValidateCapacity ENABLE;
/

CREATE OR REPLACE TRIGGER trgContainerAudit
AFTER INSERT OR UPDATE OR DELETE ON CONTAINER_CARGOMANIFEST
FOR EACH ROW
WHEN (old.PARTIAL_CARGO_MANIFEST_ID IS NOT NULL OR new.PARTIAL_CARGO_MANIFEST_ID IS NOT NULL)
    DECLARE
        vOperationType Container_AuditTrail.operation_type%type;
        vContainerNum Container_AuditTrail.container_num%type;
        vCargoManifestId Container_AuditTrail.cargo_manifest_id%type;
    BEGIN
        IF DELETING THEN
            vContainerNum := :old.CONTAINER_NUM;
            vCargoManifestId := :old.PARTIAL_CARGO_MANIFEST_ID;
            vOperationType := 'DELETE';
        ELSE
            vContainerNum := :new.CONTAINER_NUM;
            vCargoManifestId := :new.PARTIAL_CARGO_MANIFEST_ID;
            IF INSERTING THEN
                vOperationType := 'INSERT';
            ELSE
                vOperationType := 'UPDATE';
           END IF;
        END IF;
        INSERT INTO Container_AuditTrail VALUES (USER, CURRENT_TIMESTAMP, vOperationType, vContainerNum, vCargoManifestId);
    END;
/

ALTER TRIGGER trgContainerAudit ENABLE;
/