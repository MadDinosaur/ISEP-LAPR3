CREATE OR REPLACE FUNCTION funcLoadingUnloadingMap (pPortManagerId employee.system_user_code_employee%type, pStartDate cargomanifest_partial.finishing_date_time%type)
RETURN SYS_REFCURSOR
AS
    vManifests SYS_REFCURSOR;
    vPortId STORAGE.IDENTIFICATION%type;
    vOperation VARCHAR(20);
    vDate CARGOMANIFEST_PARTIAL.FINISHING_DATE_TIME%type;
    vVehicle VARCHAR(20);
    vVehicleId INTEGER;
    vNumContainers INTEGER;
    vContainerNum CONTAINER.NUM%type;
    vContainerPosition VARCHAR(20);
    vPortManager STORAGE.SYSTEM_USER_CODE_MANAGER%type;
BEGIN
    -- check if port manager is valid
    SELECT UNIQUE SYSTEM_USER_CODE_MANAGER INTO vPortManager FROM STORAGE WHERE SYSTEM_USER_CODE_MANAGER = pPortManagerId AND (SELECT NAME FROM STORAGETYPE WHERE ID = STORAGE.STORAGE_TYPE_ID) LIKE 'Port';

    -- retrive manifest information into multiline cursor
    OPEN vManifests FOR SELECT  STORAGE_IDENTIFICATION AS "Port ID",
                                (CASE WHEN LOADING_FLAG = 1 THEN 'Load' ELSE 'Unload' END) AS "Operation Type",
                                FINISHING_DATE_TIME AS "Load/Unload Date",
                                (CASE WHEN SHIP_MMSI IS NULL THEN 'Truck' ELSE 'Ship' END) AS "Vehicle",
                                NVL(SHIP_MMSI, TRUCK_ID) AS "ID",
                                COUNT(CONTAINER_NUM) OVER (PARTITION BY PARTIAL_CARGO_MANIFEST_ID) AS "No. of Containers to Load/Unload",
                                CONTAINER_NUM AS "Container No.",
                                ('(' || CONTAINER_POSITION_X || ',' || CONTAINER_POSITION_Y || ',' || CONTAINER_POSITION_Z || ')') AS "Container Position"
                        FROM CARGOMANIFEST_PARTIAL CP INNER JOIN CONTAINER_CARGOMANIFEST CC on CP.ID = CC.PARTIAL_CARGO_MANIFEST_ID
                        WHERE FINISHING_DATE_TIME BETWEEN pStartDate AND pStartDate + 7
                        AND STORAGE_IDENTIFICATION IN (SELECT STORAGE_IDENTIFICATION FROM STORAGE
                                                        WHERE SYSTEM_USER_CODE_MANAGER = pPortManagerId)
                        ORDER BY STORAGE_IDENTIFICATION, FINISHING_DATE_TIME;

    -- database output FOR TESTS ONLY
    /*DBMS_OUTPUT.PUT_LINE('Port ID | Operation Type | Load/Unload Date | Vehicle | ID | No. of Container to Load/Unload | Container No. | Container Position');

    LOOP
        FETCH vManifests INTO vPortId, vOperation, vDate, vVehicle, vVehicleId, vNumContainers, vContainerNum, vContainerPosition;
        EXIT WHEN vManifests %NOTFOUND;

        DBMS_OUTPUT.PUT_LINE(vPortId || ' | ' || vOperation || ' | ' || vDate || ' | ' || vVehicle || ' | ' || vVehicleId || ' | ' || vNumContainers || ' | ' || vContainerNum || ' | ' || vContainerPosition);
    END LOOP;*/

    RETURN vManifests;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        raise_application_error(-20006, 'Invalid port manager.');
        RETURN NULL;
END;
/

-- function test
SELECT funcLoadingUnloadingMap (9, TO_TIMESTAMP('2020-05-19 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) FROM DUAL;