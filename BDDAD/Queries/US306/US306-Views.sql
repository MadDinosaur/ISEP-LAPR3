CREATE OR REPLACE VIEW "StorageInfo" AS
SELECT CONTAINER.NUM, CARGOMANIFEST_PARTIAL.STORAGE_IDENTIFICATION, CONTAINER.MAX_VOLUME
FROM CONTAINER
INNER JOIN CONTAINER_CARGOMANIFEST ON CONTAINER_CARGOMANIFEST.CONTAINER_NUM = CONTAINER.NUM
INNER JOIN CARGOMANIFEST_PARTIAL ON CONTAINER_CARGOMANIFEST.PARTIAL_CARGO_MANIFEST_ID = CARGOMANIFEST_PARTIAL.ID
INNER JOIN STORAGE ON STORAGE.IDENTIFICATION = CARGOMANIFEST_PARTIAL.STORAGE_IDENTIFICATION
WHERE CARGOMANIFEST_PARTIAL.FINISHING_DATE_TIME IS NOT NULL AND LOADING_FLAG = 0 