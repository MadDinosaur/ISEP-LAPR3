create or replace FUNCTION func_occupancy_rate_given_moment(id_ship ship.mmsi%type,given_moment DATE)
RETURN number 
IS
    occupancy_rate number;
    manifest_id number;

BEGIN
    SELECT c.id 
    INTO manifest_id   
    FROM cargomanifest_partial c
    WHERE c.finishing_date_time = (SELECT MAX(c.finishing_date_time) 
                                   FROM cargomanifest_partial c
                                   WHERE c.finishing_date_time <= given_moment
                                   AND c.status LIKE 'finished'
                                   AND c.ship_mmsi = id_ship)
    AND C.STATUS LIKE 'finished';

    occupancy_rate := func_occupancy_rate(id_ship,manifest_id);
    RETURN occupancy_rate;


EXCEPTION 
    WHEN NO_DATA_FOUND THEN
    manifest_id  := NULL;
    RETURN NULL;

END func_occupancy_rate_given_moment;

/
-- Call function to test the function --
SELECT FUNC_OCCUPANCY_RATE_GIVEN_MOMENT(100000001,TO_TIMESTAMP('2020-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) FROM DUAL;
