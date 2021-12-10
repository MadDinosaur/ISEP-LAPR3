CREATE VIEW "Storage_Container" AS
SELECT * FROM(
       SELECT CONTAINER.NUM C1,
           --id of storage where container is located
           CARGOMANIFEST.STORAGE_IDENTIFICATION
        FROM CONTAINER
        INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM
        INNER JOIN CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CARGO_MANIFEST_ID = CARGOMANIFEST.ID
        WHERE CARGOMANIFEST.FINISHING_DATE_TIME IS NOT NULL AND LOADING_FLAG = 0
        --get most recent cargo manifest
        AND CARGOMANIFEST.FINISHING_DATE_TIME = (SELECT MAX(CARGOMANIFEST.FINISHING_DATE_TIME)
                                                FROM CARGOMANIFEST INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM
                                                WHERE CONTAINER_NUM = 1))
                                                WITH READ ONLY;