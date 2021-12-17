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
CREATE OR REPLACE PROCEDURE container_route(pClientId SHIPMENT.id%type, pContainerNum CONTAINER.num%type)
AS
    vShipmentId SHIPMENT.id%type;
    vShipmentOriginDate CARGOMANIFEST_PARTIAL.finishing_date_time%type;
    vShipmentDestinationDate CARGOMANIFEST_PARTIAL.finishing_date_time%type;
    vCargoManifest CARGOMANIFEST_PARTIAL%rowtype;
BEGIN
    -- check if identifiers are valid
    check_container_shipment(pClientId, pContainerNum);
    -- if no error raised, get shipment id
    SELECT id INTO vShipmentId FROM SHIPMENT WHERE SYSTEM_USER_CODE_CLIENT = pClientId AND CONTAINER_NUM = pContainerNum;

    SELECT PARTING_DATE INTO vShipmentOriginDate FROM SHIPMENT WHERE ID = vShipmentId;
    -- check if container has begun route
    IF vShipmentOriginDate IS NULL THEN
        DBMS_OUTPUT.PUT_LINE('Container has not begun route. Please wait for departure.');
        RETURN;
    END IF;

    -- check if container has already arrived
    SELECT NVL(ARRIVAL_DATE, CURRENT_TIMESTAMP) INTO vShipmentDestinationDate FROM SHIPMENT WHERE ID = vShipmentId;

    -- find all records between the timestamps
    DBMS_OUTPUT.PUT_LINE('Location | Operation | Mean of Transport | Timestamp');
    FOR vCargoManifest IN
        (SELECT (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = STORAGE_IDENTIFICATION) as Location,
                (CASE WHEN LOADING_FLAG = 1 THEN 'Loaded' ELSE 'Offloaded' END) as Operation,
                (CASE WHEN SHIP_MMSI IS NOT NULL THEN 'Ship' ELSE 'Truck' END) as "Mean of Transport",
                FINISHING_DATE_TIME as Timestamp
        FROM CONTAINER_CARGOMANIFEST cc INNER JOIN CARGOMANIFEST_PARTIAL cp on cc.PARTIAL_CARGO_MANIFEST_ID = cp.ID
        WHERE CONTAINER_NUM = pContainerNum
        AND FINISHING_DATE_TIME BETWEEN vShipmentOriginDate AND vShipmentDestinationDate
        ORDER BY FINISHING_DATE_TIME)
    LOOP
        DBMS_OUTPUT.PUT_LINE(vCargoManifest.Location || ' | ' || vCargoManifest.Operation || ' | ' || vCargoManifest."Mean of Transport" || ' | ' || vCargoManifest.Timestamp);
    END LOOP;
EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('No container found.');
END;
/
call container_route(6,1); -- Container in standby
call container_route(6,2); -- Container departed
call container_route(6, 3); -- Container arrived
call container_route(6, 100); -- Inexistent (throws error)
/