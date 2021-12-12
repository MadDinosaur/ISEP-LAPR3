CREATE OR REPLACE PROCEDURE containers_loaded(pShipMmsi CARGOMANIFEST_PARTIAL.ship_mmsi%type, pPort CARGOMANIFEST_PARTIAL.storage_identification%type)
AS
    vContainerNum container.num%type;
    vType varchar(20);
    vPositionX container_cargomanifest.container_position_x%type;
    vPositionY container_cargomanifest.container_position_y%type;
    vPositionZ container_cargomanifest.container_position_z%type;
    vLoad container.payload%type;
    CURSOR containers IS
        SELECT NUM,
        (CASE WHEN REFRIGERATED_FLAG = 1 THEN 'Refrigerated' ELSE 'Non-refrigerated' END) as "TYPE",
        CONTAINER_POSITION_X, CONTAINER_POSITION_Y, CONTAINER_POSITION_Z,
        PAYLOAD
        FROM CONTAINER INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER.NUM = CONTAINER_CARGOMANIFEST.CONTAINER_NUM
        WHERE PARTIAL_CARGO_MANIFEST_ID IN
          (SELECT ID
          FROM CARGOMANIFEST_PARTIAL
          WHERE SHIP_MMSI = pShipMmsi
            AND STORAGE_IDENTIFICATION = pPort
            AND LOADING_FLAG = 1
            AND FINISHING_DATE_TIME IS NULL);
BEGIN
    OPEN containers;
    DBMS_OUTPUT.PUT_LINE('Container no. | Type | Position | Load');
    LOOP
        FETCH containers INTO vContainerNum, vType, vPositionX, vPositionY, vPositionZ, vLoad;
        EXIT WHEN containers%NOTFOUND;
        DBMS_OUTPUT.PUT_LINE(vContainerNum || ' | ' || vType || ' | (' || vPositionX || ',' || vPositionY || ',' || vPositionZ || ') | ' || vLoad);
    end loop;
    CLOSE containers;
end;
/

call containers_loaded(100000001, 2);