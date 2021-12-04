-- drop existing tables to avoid naming conflicts
DROP TABLE StorageType CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage CASCADE CONSTRAINTS PURGE;
DROP TABLE Container CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate CASCADE CONSTRAINTS PURGE;
DROP TABLE Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate_Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE Container_CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE VesselType CASCADE CONSTRAINTS PURGE;
DROP TABLE Ship CASCADE CONSTRAINTS PURGE;
DROP TABLE DynamicData CASCADE CONSTRAINTS PURGE;
DROP TABLE ShipTrip CASCADE CONSTRAINTS PURGE;
DROP TABLE Captain CASCADE CONSTRAINTS PURGE;

-- create tables
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
    name                   VARCHAR(20)
        CONSTRAINT nnStorageName NOT NULL,
    continent              VARCHAR(20)
        CONSTRAINT nnContinent NOT NULL,
    country                VARCHAR(20)
        CONSTRAINT nnCountry NOT NULL,
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
    container_num        INTEGER
        CONSTRAINT nnContainerNum NOT NULL,
    cargo_manifest_id    INTEGER
        CONSTRAINT nnCargoManifestId NOT NULL,
    container_position_x INTEGER
        CONSTRAINT nnContainerPositionX NOT NULL,
    container_position_y INTEGER
        CONSTRAINT nnContainerPositionY NOT NULL,
    container_position_z INTEGER
        CONSTRAINT nnContainerPositionZ NOT NULL
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
    CONSTRAINT ckStorageOriginDestination CHECK (storage_identification_origin != storage_identification_destination)
);

CREATE TABLE CargoManifest
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkCargoManifestId PRIMARY KEY,
    ship_mmsi    NUMBER(9)
        CONSTRAINT nnCargoShipMMSI NOT NULL,
    storage_identification INTEGER,
    loading_flag NUMBER(1)
        CONSTRAINT ckLoadingFlag CHECK (loading_flag BETWEEN 0 AND 1),
    finishing_date_time    TIMESTAMP
        DEFAULT NULL,
    CONSTRAINT ckPartialManifest CHECK (
        -- Partial port cargo manifest
        (storage_identification IS NOT NULL AND loading_flag IS NOT NULL)
        -- Full ship cargo manifest
        OR (storage_identification IS NULL AND loading_flag IS NULL))
);

/*CREATE OR REPLACE TRIGGER trgUpdateCargoManifest
    BEFORE UPDATE OF finishing_date_time ON CargoManifest
    FOR EACH ROW
DECLARE
    V_Prev_Cargo_Id CARGOMANIFEST.id%type;
    V_Curr_Cargo_Id CargoManifest.id%type;
    V_Ship_Mmsi CargoManifest.ship_mmsi%type;
    V_Container_Load CONTAINER_CARGOMANIFEST%rowtype;
    V_Container_Cargo CONTAINER_CARGOMANIFEST%rowtype;
BEGIN
    -- Determine if new insert is a partial cargo manifest
    IF :new.LOADING_FLAG IS NOT NULL THEN

        BEGIN
            -- Save previous cargo manifest id
            SELECT ID INTO V_Prev_Cargo_Id FROM CARGOMANIFEST WHERE STORAGE_IDENTIFICATION IS NULL AND FINISHING_DATE_TIME IS NULL;
            -- Set finishing date on previous cargo manifest
            UPDATE CARGOMANIFEST SET finishing_date_time = SYSDATE WHERE ID = V_Prev_Cargo_Id;
            -- Generate a new ship cargo manifest
            INSERT INTO CARGOMANIFEST (ship_mmsi, storage_identification, loading_flag, finishing_date_time) VALUES (V_Ship_Mmsi, NULL, NULL, NULL);
            -- Save new cargo manifest id
            SELECT ID INTO V_Curr_Cargo_Id FROM CARGOMANIFEST WHERE STORAGE_IDENTIFICATION IS NULL AND FINISHING_DATE_TIME IS NULL;
            -- Copy containers onto new manifest
            FOR V_Container_Cargo IN
                (SELECT * FROM CONTAINER_CARGOMANIFEST WHERE cargo_manifest_id = V_Prev_Cargo_Id)
                LOOP
                    INSERT INTO CONTAINER_CARGOMANIFEST (container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (V_Container_Cargo.container_num, V_Curr_Cargo_Id, V_Container_Cargo.container_position_x, V_Container_Cargo.container_position_y, V_Container_Cargo.container_position_z);
                END LOOP;
        EXCEPTION
            -- If no previous cargo manifest exists
            WHEN NO_DATA_FOUND THEN
                -- Generate a new ship cargo manifest
                INSERT INTO CARGOMANIFEST (ship_mmsi, storage_identification, loading_flag, finishing_date_time) VALUES (V_Ship_Mmsi, NULL, NULL, NULL);
                -- Save new cargo manifest id
                SELECT ID INTO V_Curr_Cargo_Id FROM CARGOMANIFEST WHERE STORAGE_IDENTIFICATION IS NULL AND FINISHING_DATE_TIME IS NULL;
        END;
        -- Select containers to be loaded/offloaded and add/remove them from ship cargo manifest
        FOR V_Container_Load IN
            (SELECT * FROM CONTAINER_CARGOMANIFEST WHERE cargo_manifest_id = :new.ID)
            LOOP
                CASE WHEN :new.LOADING_FLAG = 1
                    THEN INSERT INTO CONTAINER_CARGOMANIFEST (container_num, cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                VALUES (V_Container_Load.container_num, V_Curr_Cargo_Id, V_Container_Load.container_position_x, V_Container_Load.container_position_y, V_Container_Load.container_position_z);
                ELSE
                    DELETE FROM CONTAINER_CARGOMANIFEST WHERE CARGO_MANIFEST_ID = V_Curr_Cargo_Id AND CONTAINER_NUM = V_Container_Load.container_num;
                END CASE;
            END LOOP;
    END IF;
END trgUpdateCargoManifest;
/
alter trigger trgUpdateCargoManifest enable;*/

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
    captain_id                              VARCHAR(10)
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

-- define foreign keys and combined primary keys
ALTER TABLE Storage
    ADD CONSTRAINT fkStorageTypeId FOREIGN KEY (storage_type_id) REFERENCES StorageType (id);

ALTER TABLE Container
    ADD CONSTRAINT fkContainerCscPlateSerialNumber FOREIGN KEY (csc_plate_serial_number) REFERENCES CscPlate (serial_number);


ALTER TABLE Container_CargoManifest
    ADD CONSTRAINT fkContainerCargoManifestContainerNum FOREIGN KEY (container_num) REFERENCES Container (num)
    ADD CONSTRAINT fkContainerCargoManifestCargoManifestId FOREIGN KEY (cargo_manifest_id) REFERENCES CargoManifest (id)
    ADD CONSTRAINT pkContainerCargoManifest PRIMARY KEY (container_num, cargo_manifest_id);

ALTER TABLE Shipment
    ADD CONSTRAINT fkShipmentStorageIdentificationOrigin FOREIGN KEY (storage_identification_origin) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentStorageIdentificationDestination FOREIGN KEY (storage_identification_destination) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentContainerNum FOREIGN KEY (container_num) REFERENCES Container (num);

ALTER TABLE CargoManifest
    ADD CONSTRAINT fkContainerStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage(identification)
    ADD CONSTRAINT fkCargoManifestShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);

ALTER TABLE Ship
    ADD CONSTRAINT fkShipFleetId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT fkShipVesselTypeId FOREIGN KEY (vessel_type_id) REFERENCES VesselType (id);

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