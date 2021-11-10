-- drop existing tables to avoid naming conflicts
DROP TABLE StorageType CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage CASCADE CONSTRAINTS PURGE;
DROP TABLE Container CASCADE CONSTRAINTS PURGE;
DROP TABLE Container_CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Storage_User_Staff CASCADE CONSTRAINTS PURGE;
DROP TABLE Role CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser_Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE SystemUser_Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE Shipment CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate CASCADE CONSTRAINTS PURGE;
DROP TABLE Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE CscPlate_Certificate CASCADE CONSTRAINTS PURGE;
DROP TABLE CargoManifest CASCADE CONSTRAINTS PURGE;
DROP TABLE Truck CASCADE CONSTRAINTS PURGE;
DROP TABLE Fleet CASCADE CONSTRAINTS PURGE;
DROP TABLE Ship CASCADE CONSTRAINTS PURGE;
DROP TABLE DynamicData CASCADE CONSTRAINTS PURGE;

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
    system_user_id_manager INTEGER
        CONSTRAINT nnSystemUserIdManager NOT NULL,
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
    CONSTRAINT ckStorageLongitude CHECK (longitude BETWEEN -180 AND 180 or latitude = 181),
    CONSTRAINT ckStorageLocation UNIQUE (latitude, longitude)
);

CREATE TABLE Container
(
    num                     INTEGER
        CONSTRAINT pkContainerNum PRIMARY KEY,
    storage_identification  INTEGER
        CONSTRAINT nnContainerStorageIdentification NOT NULL,
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
    approval_number   VARCHAR(20)
        CONSTRAINT nnApprovalNumber NOT NULL
        CONSTRAINT unApprovalNumber UNIQUE,
    acep_number       VARCHAR(20)
        CONSTRAINT nnAcepNumber NOT NULL
        CONSTRAINT unAcepNumber UNIQUE,
    data_manufactured DATE
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

CREATE TABLE Storage_User_Staff
(
    storage_identification INTEGER
        CONSTRAINT nnStorageUserStaffStorageIdentification NOT NULL,
    system_user_id         INTEGER
        CONSTRAINT nnStorageUserStaffSystemUserId NOT NULL
);

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
    id      INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkSystemUserId PRIMARY KEY,
    role_id INTEGER
        CONSTRAINT nnSystemUserRoleId NOT NULL,
    name    VARCHAR(20)
        CONSTRAINT nnSystemUserName NOT NULL,
    email   VARCHAR(40)
        CONSTRAINT nnEmail NOT NULL
        CONSTRAINT unEmail UNIQUE
        CONSTRAINT ckEmail CHECK (email LIKE '%_@__%.__%'),
    password VARCHAR(40)
);

CREATE TABLE SystemUser_Shipment
(
    system_user_id INTEGER
        CONSTRAINT nnSystemUserShipmentSystemUserId NOT NULL,
    shipment_id    INTEGER
        CONSTRAINT nnSystemUserShipmentShipmentId NOT NULL
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

CREATE TABLE SystemUser_Fleet
(
    system_user_id INTEGER
        CONSTRAINT nnSystemUserFleetSystemUserId NOT NULL,
    fleet_id       INTEGER
        CONSTRAINT nnSystemUserFleetFleetId NOT NULL
);

CREATE TABLE CargoManifest
(
    id           INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkCargoManifestId PRIMARY KEY,
    truck_id     INTEGER,
    ship_mmsi    NUMBER(9),
    CONSTRAINT ckTransportation CHECK (truck_id IS NULL OR ship_mmsi IS NULL),
    loading_flag NUMBER(1)
        CONSTRAINT nnLoadingFlag NOT NULL
        CONSTRAINT ckLoadingFlag CHECK (loading_flag BETWEEN 0 AND 1)
);

CREATE TABLE Truck
(
    id INTEGER CONSTRAINT pkTruckId PRIMARY KEY
);

CREATE TABLE Fleet
(
    id INTEGER CONSTRAINT pkFleetId PRIMARY KEY
);

CREATE TABLE Ship
(
    mmsi                                     NUMBER(9)
        CONSTRAINT pkShipMMSI PRIMARY KEY
        CONSTRAINT ckMMSI CHECK (mmsi BETWEEN 100000000 AND 999999999),
    fleet_id                                 INTEGER
        CONSTRAINT nnShipFleetId NOT NULL,
    system_user_id_captain                   INTEGER
        CONSTRAINT nnSystemUserIdCaptain NOT NULL,
    system_user_id_chief_electrical_engineer INTEGER
        CONSTRAINT nnSystemUserIdChiefElectricalEngineer NOT NULL,
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
    vessel_type                              NUMBER(2)
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
    id                INTEGER GENERATED ALWAYS AS IDENTITY
        CONSTRAINT pkDynamicDataId PRIMARY KEY,
    ship_mmsi         NUMBER(9)
        CONSTRAINT nnShipMmsi NOT NULL,
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

-- define foreign keys and combined primary keys
ALTER TABLE Storage
    ADD CONSTRAINT fkStorageSystemUserIdManager FOREIGN KEY (system_user_id_manager) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkStorageTypeId FOREIGN KEY (storage_type_id) REFERENCES StorageType (id);

ALTER TABLE Container
    ADD CONSTRAINT fkContainerStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage (identification)
    ADD CONSTRAINT fkContainerCscPlateSerialNumber FOREIGN KEY (csc_plate_serial_number) REFERENCES CscPlate (serial_number);

ALTER TABLE Container_CargoManifest
    ADD CONSTRAINT fkContainerCargoManifestContainerNum FOREIGN KEY (container_num) REFERENCES Container (num)
    ADD CONSTRAINT fkContainerCargoManifestCargoManifestId FOREIGN KEY (cargo_manifest_id) REFERENCES CargoManifest (id)
    ADD CONSTRAINT pkContainerCargoManifest PRIMARY KEY (container_num, cargo_manifest_id);

ALTER TABLE Storage_User_Staff
    ADD CONSTRAINT fkStorageUserStaffStorageIdentification FOREIGN KEY (storage_identification) REFERENCES Storage (identification)
    ADD CONSTRAINT fkStorageUserStaffSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT pkStorageUserStaff PRIMARY KEY (storage_identification, system_user_id);

ALTER TABLE Shipment
    ADD CONSTRAINT fkShipmentStorageIdentificationOrigin FOREIGN KEY (storage_identification_origin) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentStorageIdentificationDestination FOREIGN KEY (storage_identification_destination) REFERENCES Storage (identification)
    ADD CONSTRAINT fkShipmentContainerNum FOREIGN KEY (container_num) REFERENCES Container (num);

ALTER TABLE CargoManifest
    ADD CONSTRAINT fkCargoManifestTruckId FOREIGN KEY (truck_id) REFERENCES Truck (id)
    ADD CONSTRAINT fkCargoManifestShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);

ALTER TABLE SystemUser
    ADD CONSTRAINT fkSystemUserRoleId FOREIGN KEY (role_id) REFERENCES Role (id);

ALTER TABLE SystemUser_Shipment
    ADD CONSTRAINT fkSystemUserShipmentSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkSystemUserShipmentShipmentId FOREIGN KEY (shipment_id) REFERENCES Shipment (id)
    ADD CONSTRAINT pkUserShipment PRIMARY KEY (system_user_id, shipment_id);

ALTER TABLE SystemUser_Fleet
    ADD CONSTRAINT fkSystemUserFleetSystemUserId FOREIGN KEY (system_user_id) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkSystemUserFleetFleetId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT pkUserFleet PRIMARY KEY (system_user_id, fleet_id);

ALTER TABLE Ship
    ADD CONSTRAINT fkShipFleedId FOREIGN KEY (fleet_id) REFERENCES Fleet (id)
    ADD CONSTRAINT fkShipSystemUserIdCaptain FOREIGN KEY (system_user_id_captain) REFERENCES SystemUser (id)
    ADD CONSTRAINT fkShipSystemUserIdChiefElectricalEngineer FOREIGN KEY (system_user_id_chief_electrical_engineer) REFERENCES SystemUser (id);

ALTER TABLE DynamicData
    ADD CONSTRAINT fkDynamicDataShipMmsi FOREIGN KEY (ship_mmsi) REFERENCES Ship (mmsi);