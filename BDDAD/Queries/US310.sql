CREATE OR REPLACE PROCEDURE occupation_map_given_month (pStorageId storage.identification%type, pMonth number, pYear number)
AS
    vOccupancyRate number;
    vStorageCapacity storage.max_volume%type;
    vContainerVolume container.max_volume%type;
    vDate cargomanifest_partial.finishing_date_time%type;

    -- get dates in the given month where occupancy rates changed
    CURSOR vOccupancyDates IS (SELECT FINISHING_DATE_TIME
                                FROM CARGOMANIFEST_PARTIAL
                                WHERE STATUS LIKE 'finished'
                                AND STORAGE_IDENTIFICATION = pStorageId
                                AND EXTRACT(MONTH FROM FINISHING_DATE_TIME) = pMonth
                                AND EXTRACT(YEAR FROM FINISHING_DATE_TIME) = pYear
                                UNION
                                SELECT TO_TIMESTAMP(pYear || '-' || pMonth || '-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS') AS FINISHING_DATE_TIME FROM DUAL);

BEGIN
    -- get storage capacity
    SELECT MAX_VOLUME INTO vStorageCapacity FROM STORAGE WHERE IDENTIFICATION = pStorageId;

    DBMS_OUTPUT.PUT_LINE('Storage no. ' || pStorageId || ' Occupancy Map for ' || pMonth || '/' || pYear);
    DBMS_OUTPUT.PUT_LINE('Date | Occupancy Rate');

    OPEN vOccupancyDates;

    LOOP
        FETCH vOccupancyDates INTO vDate;
        EXIT WHEN vOccupancyDates%NOTFOUND;

        -- get total container occupancy per date
        SELECT SUM(CONTAINERVOLUMEDIFF)
        INTO vContainerVolume
        FROM (SELECT cp.FINISHING_DATE_TIME as STOCKDATE, SUM((CASE WHEN cp.LOADING_FLAG = 1 THEN -con.MAX_VOLUME ELSE con.MAX_VOLUME END)) as CONTAINERVOLUMEDIFF
                FROM CONTAINER con, CARGOMANIFEST_PARTIAL cp, CONTAINER_CARGOMANIFEST cc
                WHERE cc.PARTIAL_CARGO_MANIFEST_ID = cp.ID
                AND cc.CONTAINER_NUM = con.NUM
                AND FINISHING_DATE_TIME IS NOT NULL
                AND cp.STATUS like 'finished'
                AND cp.STORAGE_IDENTIFICATION = pStorageId
                GROUP BY cp.FINISHING_DATE_TIME)
        WHERE STOCKDATE <= vDate;

        -- calculate occupancy rate per date
        vOccupancyRate := ROUND(vContainerVolume/vStorageCapacity * 100,2);
        IF vOccupancyRate < 0 THEN vOccupancyRate := 0; END IF;

        DBMS_OUTPUT.PUT_LINE(vDate || ' | ' || vOccupancyRate || '%');
    END LOOP;

    CLOSE vOccupancyDates;
END;
/
-- Tests
CALL occupation_map_given_month(3, 12, 2021);