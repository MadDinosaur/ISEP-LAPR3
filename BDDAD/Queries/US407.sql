CREATE OR REPLACE FUNCTION funcLoadingUnloadingMap (pPortManagerId employee.system_user_code_employee%type)
RETURN SYS_REFCURSOR
AS
    vManifests SYS_REFCURSOR;
BEGIN
    -- get all relevant manifests in the next week
    OPEN vManifests FOR SELECT FINISHING_DATE_TIME AS "Load/Unload Date",
                                NVL(SHIP_MMSI, TRUCK_ID) AS "Ship/Truck",
                                SUM(CONTAINER_NUM) AS "No. of Containers to Load/Unload"
                        FROM CARGOMANIFEST_PARTIAL CP INNER JOIN CONTAINER_CARGOMANIFEST CC on CP.ID = CC.PARTIAL_CARGO_MANIFEST_ID
                        WHERE FINISHING_DATE_TIME BETWEEN CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP + 7
                        AND STORAGE_IDENTIFICATION IN (SELECT STORAGE_IDENTIFICATION FROM STORAGE
                                                        WHERE SYSTEM_USER_CODE_MANAGER = pPortManagerId)
                        GROUP BY FINISHING_DATE_TIME, NVL(SHIP_MMSI, TRUCK_ID)
                        ORDER BY FINISHING_DATE_TIME;
    RETURN vManifests;
END;
/
SELECT funcLoadingUnloadingMap ('9') FROM DUAL;