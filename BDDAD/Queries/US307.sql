CREATE OR REPLACE TRIGGER trgWarehouseCapacity
    AFTER UPDATE ON CARGOMANIFEST_PARTIAL
    FOR EACH ROW
    WHEN (new.FINISHING_DATE_TIME IS NOT NULL AND new.LOADING_FLAG = 0)

    DECLARE
        varOccupancyNumber number;
        varManifestId CARGOMANIFEST_FULL.ID%type;

        varStorageCapacity STORAGE.MAX_VOLUME%type;
        varContainerVolume CONTAINER.MAX_VOLUME%type;

    BEGIN
        -- get full cargo manifest id, estimated for the partial manifest's date
        BEGIN
            SELECT c.id
            INTO varManifestId
            FROM cargomanifest_full c
            WHERE c.finishing_date_time = (SELECT MAX(cf.finishing_date_time)
                                           FROM cargomanifest_full cf
                                           WHERE cf.finishing_date_time <= :new.finishing_date_time
                                           AND cf.STORAGE_IDENTIFICATION = :new.STORAGE_IDENTIFICATION)
            AND c.STORAGE_IDENTIFICATION = :new.STORAGE_IDENTIFICATION
            AND c.status NOT LIKE 'finished';
        EXCEPTION
            -- if there are no estimated full cargo manifests, get the current one
            WHEN NO_DATA_FOUND THEN
                SELECT c.id
                INTO varManifestId
                FROM cargomanifest_full c
                WHERE c.finishing_date_time IS NULL
                AND c.STORAGE_IDENTIFICATION = :new.STORAGE_IDENTIFICATION
                AND c.status IS NULL;
        END;

        -- get container volumes and ship capacity
        SELECT (s.MAX_VOLUME), sum(con.max_volume)
        INTO varStorageCapacity, varContainerVolume
        FROM cargomanifest_full c, container_cargoManifest cc, storage s, container con
        WHERE s.IDENTIFICATION = :new.STORAGE_IDENTIFICATION
        AND c.id = varManifestId
        AND c.STORAGE_IDENTIFICATION = s.IDENTIFICATION
        AND cc.full_cargo_manifest_id = c.id
        AND cc.container_num = con.num
        GROUP BY s.MAX_VOLUME;

        varOccupancyNumber := varContainerVolume/varStorageCapacity;

        -- check if insert causes overloading and reverse the operation if it does
         IF (varOccupancyNumber > 1) THEN
            RAISE_APPLICATION_ERROR(-20015,'Containers in cargo manifest exceed storage capacity. Please remove containers from manifest or issue a loading order.');
         END IF;

     EXCEPTION
        WHEN NO_DATA_FOUND THEN
            varOccupancyNumber := 0;
    END;
/
ALTER TRIGGER trgWarehouseCapacity ENABLE;
/

-- Test - throws trgWarehouseCapacity error
DECLARE
    varStorageId storage.identification%type;
    varPartialId cargomanifest_partial.id%type;
    varFullId cargomanifest_full.id%type;
BEGIN
    varStorageId := 4;

    -- create new cargo manifest
    INSERT INTO CARGOMANIFEST_PARTIAL (SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG) VALUES (100000002, varStorageId, 0);
    SELECT id INTO varPartialId FROM CARGOMANIFEST_PARTIAL WHERE SHIP_MMSI = 100000002 AND STORAGE_IDENTIFICATION = varStorageId AND LOADING_FLAG = 0;

    -- load overweight container
    INSERT INTO CONTAINER_CARGOMANIFEST (CONTAINER_NUM, PARTIAL_CARGO_MANIFEST_ID, CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z)
    VALUES (11, varPartialId, 4, 4, 4);

    -- set estimated date on cargo manifest (triggers tgrValidateCapacity)
    UPDATE CARGOMANIFEST_PARTIAL SET FINISHING_DATE_TIME = CURRENT_TIMESTAMP WHERE ID = varPartialId;

    ROLLBACK;
END;
/


