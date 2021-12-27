CREATE OR REPLACE TRIGGER tgrValidateCapacity
    AFTER UPDATE ON CARGOMANIFEST_PARTIAL
    FOR EACH ROW
    WHEN (new.FINISHING_DATE_TIME IS NOT NULL AND new.LOADING_FLAG = 1)

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
            AND status IS NULL OR status NOT LIKE 'finished';
        EXCEPTION
            -- if there are no estimated full cargo manifests, get the current one
            WHEN NO_DATA_FOUND THEN
                SELECT c.id
                INTO varManifestId
                FROM cargomanifest_full c
                WHERE c.finishing_date_time IS NULL
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

-- Test - throws TGRVALIDATECAPACITY error
DECLARE
    varShipMMSI ship.mmsi%type;
    varPartialId cargomanifest_partial.id%type;
    varFullId cargomanifest_full.id%type;
BEGIN
    varShipMMSI := 100000002;

    -- create new cargo manifest
    INSERT INTO CARGOMANIFEST_PARTIAL (SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG) VALUES (varShipMMSI, 1, 1);
    SELECT id INTO varPartialId FROM CARGOMANIFEST_PARTIAL WHERE SHIP_MMSI = varShipMMSI AND STORAGE_IDENTIFICATION = 1 AND LOADING_FLAG = 1;

    -- load overweight container
    INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
    VALUES (11, varPartialId, 4, 4, 4);

    -- set estimated date on cargo manifest (triggers tgrValidateCapacity)
    UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = CURRENT_TIMESTAMP WHERE ID = varPartialId;
    SELECT id INTO varFullId FROM CARGOMANIFEST_FULL WHERE SHIP_MMSI = varShipMMSI;

    ROLLBACK;
END;
/