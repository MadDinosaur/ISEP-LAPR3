CREATE OR REPLACE FUNCTION funcLoadingUnloadingMap (pPortManagerId employee.system_user_code_employee%type, pStartDate cargomanifest_partial.finishing_date_time%type)
RETURN SYS_REFCURSOR
AS
    vManifests SYS_REFCURSOR;
BEGIN
    OPEN vManifests FOR SELECT FINISHING_DATE_TIME AS "Load/Unload Date",
                                NVL(SHIP_MMSI, TRUCK_ID) AS "Ship/Truck",
                                COUNT(CONTAINER_NUM) AS "No. of Containers to Load/Unload",
                                CONTAINER_NUM AS "Container No.",
                                ('(' || CONTAINER_POSITION_X || ',' || CONTAINER_POSITION_Y || ',' || CONTAINER_POSITION_Z || ')') AS "Container Position"
                        FROM CARGOMANIFEST_PARTIAL CP INNER JOIN CONTAINER_CARGOMANIFEST CC on CP.ID = CC.PARTIAL_CARGO_MANIFEST_ID
                        WHERE FINISHING_DATE_TIME BETWEEN pStartDate AND pStartDate + 7
                        AND STORAGE_IDENTIFICATION IN (SELECT STORAGE_IDENTIFICATION FROM STORAGE
                                                        WHERE SYSTEM_USER_CODE_MANAGER = pPortManagerId)
                        GROUP BY FINISHING_DATE_TIME, NVL(SHIP_MMSI, TRUCK_ID), CONTAINER_NUM, ('(' || CONTAINER_POSITION_X || ',' || CONTAINER_POSITION_Y || ',' || CONTAINER_POSITION_Z || ')')
                        ORDER BY FINISHING_DATE_TIME;
    RETURN vManifests;
END;
/
-- function test
SELECT funcLoadingUnloadingMap ('9', TO_TIMESTAMP('2021-12-01 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) FROM DUAL;
-- query test
SELECT FINISHING_DATE_TIME AS "Load/Unload Date",
        NVL(SHIP_MMSI, TRUCK_ID) AS "Ship/Truck",
        COUNT(CONTAINER_NUM) AS "No. of Containers to Load/Unload",
        CONTAINER_NUM AS "Container No.",
        ('(' || CONTAINER_POSITION_X || ',' || CONTAINER_POSITION_Y || ',' || CONTAINER_POSITION_Z || ')') AS "Container Position"
FROM CARGOMANIFEST_PARTIAL CP INNER JOIN CONTAINER_CARGOMANIFEST CC on CP.ID = CC.PARTIAL_CARGO_MANIFEST_ID
WHERE FINISHING_DATE_TIME BETWEEN TO_TIMESTAMP('2021-12-01 7:59:23', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP('2021-12-01 7:59:23', 'YYYY-MM-DD HH24:MI:SS') + 7
AND STORAGE_IDENTIFICATION IN (SELECT STORAGE_IDENTIFICATION FROM STORAGE
                                WHERE SYSTEM_USER_CODE_MANAGER = 9)
GROUP BY FINISHING_DATE_TIME, NVL(SHIP_MMSI, TRUCK_ID), CONTAINER_NUM, ('(' || CONTAINER_POSITION_X || ',' || CONTAINER_POSITION_Y || ',' || CONTAINER_POSITION_Z || ')')
ORDER BY FINISHING_DATE_TIME;

-- ALTERNATIVE --
CREATE OR REPLACE PROCEDURE prcLoadingUnloadingMap (pPortManagerId employee.system_user_code_employee%type, pStartDate cargomanifest_partial.finishing_date_time%type)
AS
    vNumContainers INTEGER;
    vContainer CONTAINER_CARGOMANIFEST%rowtype;
    vManifest CARGOMANIFEST_PARTIAL%rowtype;
BEGIN
    FOR vManifest IN (SELECT * FROM CARGOMANIFEST_PARTIAL
                        WHERE FINISHING_DATE_TIME BETWEEN pStartDate AND pStartDate + 7
                        AND STORAGE_IDENTIFICATION IN (SELECT STORAGE_IDENTIFICATION FROM STORAGE
                                                        WHERE SYSTEM_USER_CODE_MANAGER = pPortManagerId)
                        ORDER BY FINISHING_DATE_TIME)
    LOOP
        SELECT COUNT(CONTAINER_NUM) INTO vNumContainers FROM CONTAINER_CARGOMANIFEST WHERE PARTIAL_CARGO_MANIFEST_ID = vManifest.ID;

        DBMS_OUTPUT.PUT_LINE('Operation Type | Operation Date | Transport Type | Transport ID | No. of Containers');
        DBMS_OUTPUT.PUT_LINE(CASE WHEN vManifest.LOADING_FLAG = 1 THEN 'Load' ELSE 'Unload' END || ' | ' ||
        vManifest.FINISHING_DATE_TIME || ' | ' ||
        CASE WHEN vManifest.SHIP_MMSI IS NULL THEN 'Truck' ELSE 'Ship' END || ' | ' ||
        NVL(vManifest.SHIP_MMSI, vManifest.TRUCK_ID) || ' | ' ||
        vNumContainers);

        FOR vContainer IN (SELECT * FROM CONTAINER_CARGOMANIFEST WHERE PARTIAL_CARGO_MANIFEST_ID = vManifest.ID)
            LOOP
                DBMS_OUTPUT.PUT_LINE('Container No. | Container Position ');
                DBMS_OUTPUT.PUT_LINE( vContainer.CONTAINER_NUM || ' | (' || vContainer.CONTAINER_POSITION_X || ',' || vContainer.CONTAINER_POSITION_Y || ',' || vContainer.CONTAINER_POSITION_Z || ')');
            end loop;
    end loop;
    DBMS_OUTPUT.PUT_LINE(' ');
end;
/

CALL prcLoadingUnloadingMap ('9', TO_TIMESTAMP('2021-12-01 7:59:23', 'YYYY-MM-DD HH24:MI:SS'));
