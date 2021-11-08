-- drop existing tables to avoid naming conflicts
DROP TABLE StorageType CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage CASCADE CONSTRAINTS PURGE;
DROP TABLE Container CASCADE CONSTRAINTS PURGE;
DROP TABLE Container_CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage_User_Staff CASCADE CONSTRAINTS PURGE;
DROP TABLE Role CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser_Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE User_Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate CASCADE CONSTRAINTS PURGE;
DROP TABLE Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE cscPlate_Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Truck CASCADE CONSTRAINTS PURGE;
DROP TABLE Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE Ship CASCADE CONSTRAINTS PURGE;
DROP TABLE DynamicData CASCADE CONSTRAINTS PURGE;

-- create tables
CREATE TABLE StorageType
(
    id   INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkStorageTypeId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnName NOT NULL
        CONSTRAINT unName UNIQUE
);

CREATE TABLE Storage
(
    identification         INT(10)
        CONSTRAINT pkStorageIdentification PRIMARY KEY,
    system_user_id_manager INT(10)
        CONSTRAINT nnSystemUserIdManager NOT NULL,
    storage_type_id        INT(10)
        CONSTRAINT nnStorageTypeId NOT NULL,
    name                   VARCHAR(20)
        CONSTRAINT nnName NOT NULL,
    continent              VARCHAR(20)
        CONSTRAINT nnContinent NOT NULL,
    country                VARCHAR(20)
        CONSTRAINT nnCountry NOT NULL,
    latitude               NUMBER(4, 2)
        CONSTRAINT nnLatitude NOT NULL,
    CONSTRAINT ckLatitude CHECK (latitude BETWEEN -90 AND 91),
    longitude              NUMBER(5, 2)
        CONSTRAINT nnLongitude NOT NULL,
    CONSTRAINT ckLongitude CHECK (longitude BETWEEN -180 AND 181),
    CONSTRAINT ckStorageLocation UNIQUE (latitude, longitude)
);

CREATE TABLE Container
(
    num                     INT(10)
        CONSTRAINT pkContainerNum PRIMARY KEY,
    storage_identification  INT(10)
        CONSTRAINT nnStorageIdentification NOT NULL,
    csc_plate_serial_number INT(10)
        CONSTRAINT nnCscPlateSerialNumber NOT NULL,
    check_digit             INT(1)
        CONSTRAINT nnCheckDigit NOT NULL,
    iso_code                VARCHAR(4)
        CONSTRAINT nnIsoCode NOT NULL,
    gross_weight            INT(7)
        CONSTRAINT nnGrossWeight NOT NULL,
    CONSTRAINT ckGrossWeight CHECK (gross_weight >= 0),
    tare_weight             INT(7)
        CONSTRAINT nnTareWeight NOT NULL,
    CONSTRAINT ckTareWeight CHECK (tare_weight >= 0),
    payload                 INT(7)
        CONSTRAINT nnPayload NOT NULL,
    CONSTRAINT ckPayload CHECK (payload >= 0),
    max_volume              NUMBER(4, 1)
        CONSTRAINT nnMaxVolume NOT NULL,
    CONSTRAINT ckMaxVolume CHECK (max_volume >= 0),
    refrigerated_flag       INT(1)
        CONSTRAINT nnRefrigeratedFlag NOT NULL,
    CONSTRAINT ckRefrigerated CHECK (refrigerated_flag BETWEEN 0 AND 1)
);

CREATE TABLE CscPlate
(
    serial_number     INT(10)
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
    approval_number   VARCHAR(20)
        CONSTRAINT nnApprovalNumber NOT NULL
        CONSTRAINT unApprovalNumber UNIQUE,
    acep_number       VARCHAR(20)
        CONSTRAINT nnAcepNumber NOT NULL
        CONSTRAINT unAcepNumber UNIQUE,
    data_manufactured DATE
        CONSTRAINT nnDateManufactured NOT NULL,
    max_gross_mass    INT(5)
        CONSTRAINT nnMaxGrossMass NOT NULL,
    stacking_weight   INT(6)
        CONSTRAINT nnStackingWeight NOT NULL,
    racking_test      INT(5)
        CONSTRAINT nnRackingTest NOT NULL
);

CREATE TABLE Certificate
(
    id   INT(10)
        CONSTRAINT pkCertificateId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnName NOT NULL
);

CREATE TABLE CscPlate_Certificate
(
    csc_plate_serial_number INT(10)
        CONSTRAINT nnCscPlateSerialNumber NOT NULL,
    certificate_id          INT(10)
        CONSTRAINT nnCertificateId NOT NULL
);

CREATE TABLE Container_CargoManifest
(
    container_num        INT(10)
        CONSTRAINT nnContainerNum NOT NULL,
    cargo_manifest_id    INT(10)
        CONSTRAINT nnCargoManifestId NOT NULL,
    container_position_x INT(10)
        CONSTRAINT nnContainerPositionX NOT NULL,
    container_position_y INT(10)
        CONSTRAINT nnContainerPositionY NOT NULL,
    container_position_z INT(10)
        CONSTRAINT nnContainerPositionZ NOT NULL
);

CREATE TABLE Storage_User_Staff
(
    storage_identification INT(10)
        CONSTRAINT nnStorageIdentification NOT NULL,
    system_user_id         INT(10)
        CONSTRAINT nnSystemUserId NOT NULL
);

CREATE TABLE Role
(
    id   INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkRoleId PRIMARY KEY,
    name VARCHAR(20)
        CONSTRAINT nnName NOT NULL
        CONSTRAINT unName UNIQUE
);

CREATE TABLE SystemUser
(
    id      INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkSystemUserId PRIMARY KEY,
    role_id INT(10)
        CONSTRAINT nnRoleId NOT NULL,
    name    VARCHAR(20)
        CONSTRAINT nnName NOT NULL,
    email   VARCHAR(40)
        CONSTRAINT nnEmail NOT NULL
        CONSTRAINT unEmail UNIQUE
        CONSTRAINT ckEmail CHECK (email LIKE '%_@__%.__%')
);

CREATE TABLE User_Shipment
(
    system_user_id INT(10)
        CONSTRAINT nnSystemUserId NOT NULL,
    shipment_id    INT(10)
        CONSTRAINT nnShipmentId NOT NULL
);

CREATE TABLE Shipment
(
    id                                 INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkShipmentId PRIMARY KEY,
    container_num                      INT(10)
        CONSTRAINT nnContainerNum NOT NULL,
    storage_identification_origin      INT(10)
        CONSTRAINT nnStorageIdentificationOrigin NOT NULL,
    storage_identification_destination INT(10)
        CONSTRAINT nnStorageIdentificationDestination NOT NULL,
    CONSTRAINT ckStorageOriginDestination CHECK (storage_identification_origin != storage_identification_destination)
);

CREATE TABLE SystemUser_Fleet
(
    system_user_id INT(10)
        CONSTRAINT nnSystemUserId NOT NULL,
    fleet_id       INT(10)
        CONSTRAINT nnFleetId NOT NULL
);

CREATE TABLE CargoManifest
(
    id           INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkCargoManifestId PRIMARY KEY,
    truck_id     INT(10),
    ship_mmsi    INT(9),
    loading_flag INT(1)
        CONSTRAINT nnLoadingFlag NOT NULL,
    CONSTRAINT ckLoadingFlag CHECK (loading_flag BETWEEN 0 AND 1)
);

CREATE TABLE Truck
(
    id INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkTruckId PRIMARY KEY
);

CREATE TABLE Fleet
(
    id INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkFleetId PRIMARY KEY
);

CREATE TABLE Ship
(
    mmsi                                     INT(9)
        CONSTRAINT pkShipMMSI PRIMARY KEY
        CONSTRAINT ckMMSI CHECK (mmsi BETWEEN 100000000 AND 999999999),
    fleet_id                                 INT(10)
        CONSTRAINT nnFleetId NOT NULL,
    system_user_id_captain                   INT(10)
        CONSTRAINT nnSystemUserIdCaptain NOT NULL,
    system_user_id_chief_electrical_engineer INT(10)
        CONSTRAINT nnSystemUserIdChiefElectricalEngineer NOT NULL,
    name                                     VARCHAR(20)
        CONSTRAINT nnName NOT NULL,
    imo                                      INT(7)
        CONSTRAINT nnImo NOT NULL
        CONSTRAINT unImo UNIQUE
        CONSTRAINT ckIMO CHECK (imo BETWEEN 1000000 AND 9999999),
    num_generator                            INT(3)
        CONSTRAINT nnNumGenerator NOT NULL
        CONSTRAINT ckNumGenerator CHECK (num_generator >= 0),
    gen_power                                NUMBER(5, 2)
        CONSTRAINT nnGenPower NOT NULL
        CONSTRAINT ckGenPower CHECK (gen_power >= 0),
    callsign                                 VARCHAR(8)
        CONSTRAINT nnCallsign NOT NULL
        CONSTRAINT unCallsign UNIQUE,
    vessel_type                              INT(2)
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
        CONSTRAINT ckDraft CHECK (draft >= 0)
);

CREATE TABLE DynamicData
(
    id                INT(10) GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkDynamicDataId PRIMARY KEY,
    ship_mmsi         INT(9)
        CONSTRAINT nnShipMmsi NOT NULL,
    base_date_time    TIMESTAMP
        CONSTRAINT nnBaseDateTime NOT NULL
        CONSTRAINT ckBaseDateTime CHECK (base_date_time <= CURRENT_TIMESTAMP),
    latitude          NUMBER(4, 2)
        CONSTRAINT nnLatitute NOT NULL
        CONSTRAINT ckLatitude CHECK (latitude BETWEEN -90 AND 91),
    longitude         NUMBER(5, 2)
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
    position          INT(10),
    transceiver_class VARCHAR(1)
        CONSTRAINT nnTransceiverClass NOT NULL
);

-- define foreign keys and combined primary keys
ALTER TABLE Storage
    ADD CONSTRAINT fkSystemUserIdManager FOREIGN KEY (system_user_id_manager) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkStorageTypeId FOREIGN KEY (storage_type_id) REFERENCES StorageType (id);

ALTER TABLE Container
    ADD CONSTRAINT fkStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage (identification)
    ADD CONSTRAINT fkCscPlateSerialNumber FOREIGN KEY (csc_plate_serial_number) REFERENCES CscPlate (serial_number);

ALTER TABLE Container_CargoManifest
    ADD CONSTRAINT fkContainerNum FOREIGN KEY (container_num) REFERENCES Container (num)
    ADD CONSTRAINT fkCargoManifestId FOREIGN KEY (cargo_manifest_id) REFERENCES CargoManifest (id)
    ADD CONSTRAINT pkContainerCargoManifest PRIMARY KEY (container_num, cargo_manifest_id);

ALTER TABLE Storage_User_Staff
    ADD CONSTRAINT fkStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage (identification)
    ADD CONSTRAINT fkSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT pkStorageUserStaff PRIMARY KEY (storage_identification, system_user_id);

ALTER TABLE Shipment
    ADD CONSTRAINT fkStorageIdentificationOrigin FOREIGN KEY (storage_identification_origin) REFERENCES Storage (identification)
    ADD CONSTRAINT fkStorageIdentificationDestination FOREIGN KEY (storage_identification_destination) REFERENCES Storage (identification)
    ADD CONSTRAINT fkContainerNum FOREIGN KEY (container_num) REFERENCES Container (num);

ALTER TABLE CargoManifest
    ADD CONSTRAINT fkTruckId FOREIGN KEY (truck_id) REFERENCES Truck (id)
    ADD CONSTRAINT fkShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);

ALTER TABLE SystemUser
    ADD CONSTRAINT fkRoleId FOREIGN KEY (role_id) REFERENCES Role (id);

ALTER TABLE User_Shipment
    ADD CONSTRAINT fkSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkShipmentId FOREIGN KEY (shipment_id) REFERENCES Shipment (id)
    ADD CONSTRAINT pkUserShipment PRIMARY KEY (system_user_id, shipment_id);

ALTER TABLE SystemUser_Fleet
    ADD CONSTRAINT fkSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkFleetId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT pkUserFleet PRIMARY KEY (system_user_id, fleet_id);

ALTER TABLE Ship
    ADD CONSTRAINT fkFleedId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT fkSystemUserIdCaptain FOREIGN KEY (system_user_id_captain) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkSystemUserIdChiefElectricalEngineer FOREIGN KEY (system_user_id_chief_electrical_engineer) REFERENCES SystemUser (id);

ALTER TABLE DynamicData
    ADD CONSTRAINT fkShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);