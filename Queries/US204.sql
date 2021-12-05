CREATE OR REPLACE PROCEDURE container_status(pContainer_Num container.num%type)
AS
    vContainer_Num container.num%type;
    vLocationType varchar(20);
    vLocationName varchar(20);
BEGIN
    SELECT * INTO vContainer_Num, vLocationType, vLocationName FROM(
       SELECT CONTAINER.NUM as CONTAINER_NUM,
           --type of container location (port/ship)
           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,
           --name of ship or port where container is located
           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CARGOMANIFEST.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CARGOMANIFEST.STORAGE_IDENTIFICATION) END) as LOCATION_NAME
        FROM CONTAINER
        INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM
        INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID
        WHERE CONTAINER.NUM = pContainer_Num AND CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL AND LOADING_FLAG IS NOT NULL
        --get most recent cargo manifest
        ORDER BY CARGOMANIFEST.FINISHING_DATE_TIME DESC)
    WHERE ROWNUM = 1;
    DBMS_OUTPUT.PUT_LINE('Container No. | Location Type | Location Name');
    DBMS_OUTPUT.PUT_LINE(vContainer_Num || ' | ' || vLocationType || ' | ' || vLocationName);
END;
/

call CONTAINER_STATUS(1);