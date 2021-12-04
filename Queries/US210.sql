CREATE OR REPLACE PROCEDURE available_ship_mon
AS
BEGIN
    FOR ship IN (
        SELECT st1.ship_mmsi, st1.storage_identification_destination
        FROM shiptrip st1
        LEFT JOIN shiptrip st2
            ON (
                st1.ship_mmsi = st2.ship_mmsi
                AND st1.arrival_date < st2.arrival_date
            )
        WHERE st2.arrival_date IS NULL
            AND NEXT_DAY(CURRENT_DATE, 'SEGUNDA') > st1.arrival_date)
    LOOP
        DBMS_OUTPUT.PUT_LINE('Ship mmsi: ' || ship.ship_mmsi || ' - Location (Port identification): ' || ship.storage_identification_destination);
    END LOOP;
END;