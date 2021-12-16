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

SELECT * FROM SHIPTRIP WHERE STATUS = 'in progress';

-- Will throw the exception --
INSERT INTO CARGOMANIFEST_PARTIAL(ship_mmsi, storage_identification, loading_flag, finishing_date_time) VALUES (100000004, 3, 1, NULL);

-- Will not throw the exeception --
INSERT INTO CARGOMANIFEST_PARTIAL(ship_mmsi, storage_identification, loading_flag, finishing_date_time) VALUES (100000002, 3, 1, NULL);

