CREATE OR REPLACE TRIGGER trgUpdateShipmentDates
    AFTER UPDATE ON CargoManifest_Partial
    FOR EACH ROW
    WHEN (old.finishing_date_time IS NULL AND new.finishing_date_time IS NOT NULL)
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
    END;
/
ALTER TRIGGER trgUpdateShipmentDates ENABLE;
/
CREATE OR REPLACE PROCEDURE container_route(pShipmentId SHIPMENT.id%type, pContainerNum CONTAINER.num%type)
AS
    vShipmentOriginDate CARGOMANIFEST_PARTIAL.finishing_date_time%type;
    vShipmentDestinationDate CARGOMANIFEST_PARTIAL.finishing_date_time%type;
    vCargoManifest CARGOMANIFEST_PARTIAL%rowtype;
BEGIN
    SELECT PARTING_DATE INTO vShipmentOriginDate FROM SHIPMENT WHERE ID = pShipmentId;
    -- check if container has begun route
    IF vShipmentOriginDate IS NULL THEN
        DBMS_OUTPUT.PUT_LINE('Container is at source location. Please wait for departure.');
        RETURN;
    END IF;

    SELECT ARRIVAL_DATE INTO vShipmentDestinationDate FROM SHIPMENT WHERE ID = pShipmentId;
    -- check if container has already arrived
    IF vShipmentDestinationDate IS NULL THEN
        vShipmentOriginDate := CURRENT_TIMESTAMP;
    END IF;

    -- find all records between the timestamps
    FOR vCargoManifest IN
        (SELECT PARTIAL_CARGO_MANIFEST_ID, SHIP_MMSI, STORAGE_IDENTIFICATION, LOADING_FLAG, FINISHING_DATE_TIME
        FROM CONTAINER_CARGOMANIFEST cc INNER JOIN CARGOMANIFEST_PARTIAL cp on cc.PARTIAL_CARGO_MANIFEST_ID = cp.ID
        WHERE CONTAINER_NUM = pContainerNum
        AND FINISHING_DATE_TIME BETWEEN vShipmentOriginDate AND vShipmentDestinationDate
        ORDER BY FINISHING_DATE_TIME)
    LOOP
        DBMS_OUTPUT.PUT_LINE('Cargo Manifest Id | Loading | Storage Id | Ship Mmsi | Timestamp');
        DBMS_OUTPUT.PUT_LINE(vCargoManifest.PARTIAL_CARGO_MANIFEST_ID || ' | ' || vCargoManifest.LOADING_FLAG || ' | ' || vCargoManifest.STORAGE_IDENTIFICATION || ' | ' || vCargoManifest.SHIP_MMSI || ' | ' || vCargoManifest.FINISHING_DATE_TIME);
    END LOOP;
END;
/

call container_route(22,6);

