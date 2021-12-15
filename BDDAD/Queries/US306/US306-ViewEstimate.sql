CREATE OR REPLACE VIEW "StorageEstimate" AS
SELECT CONTAINER.NUM, SHIPMENT.ID, SHIPMENT.STORAGE_IDENTIFICATION_ORIGIN, SHIPMENT.PARTING_DATE, SHIPMENT.ARRIVAL_DATE
FROM CONTAINER
INNER JOIN SHIPMENT ON CONTAINER.NUM = SHIPMENT.CONTAINER_NUM
INNER JOIN STORAGE ON STORAGE.IDENTIFICATION = SHIPMENT.STORAGE_IDENTIFICATION_ORIGIN
WHERE SHIPMENT.PARTING_DATE IS NOT NULL
AND SHIPMENT.PARTING_DATE < CURRENT_TIMESTAMP + 30
AND SHIPMENT.PARTING_DATE >= CURRENT_TIMESTAMP
AND STORAGE.IDENTIFICATION = SHIPMENT.STORAGE_IDENTIFICATION_ORIGIN
GROUP BY CONTAINER.NUM, SHIPMENT.ID, SHIPMENT.STORAGE_IDENTIFICATION_ORIGIN, SHIPMENT.PARTING_DATE, SHIPMENT.ARRIVAL_DATE

-- Call Function to obtain estimate of all Warehouses -- 

select storage_identification_origin, count(*) as Leaving
from "StorageEstimate"
where storage_identification_origin > 0
group by storage_identification_origin