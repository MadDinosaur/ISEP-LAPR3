SELECT * FROM (
    SELECT CONTAINER.NUM as CONTAINER_NUM,
           --type of container location (port/ship)
           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN 'Ship' ELSE 'Port' END) as LOCATION_TYPE,
           --name of ship or port where container is located
           (CASE WHEN CARGOMANIFEST.LOADING_FLAG = 1 THEN (SELECT NAME FROM SHIP WHERE MMSI = CARGOMANIFEST.SHIP_MMSI) ELSE (SELECT NAME FROM STORAGE WHERE IDENTIFICATION = CARGOMANIFEST.STORAGE_IDENTIFICATION) END) as LOCATION_NAME,
    FROM CONTAINER
    INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM
    INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID
    WHERE CONTAINER.NUM = 1 AND CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL
    --get most recent cargo manifest
    ORDER BY CARGOMANIFEST.FINISHING_DATE_TIME DESC)
WHERE ROWNUM = 1;