create or replace FUNCTION func_occupancy_rate_given_moment(id_ship ship.mmsi%type,given_moment DATE)
RETURN number 
IS
    occupancy_rate number;
    manifest_id number;

BEGIN
    SELECT c.id 
    INTO manifest_id   
    FROM cargomanifest c
    WHERE c.finishing_date_time = (SELECT MAX(c.finishing_date_time) 
                                   FROM cargomanifest c 
                                   WHERE c.finishing_date_time <= given_moment)
                                   AND c.ship_mmsi = id_ship;

    occupancy_rate := func_occupancy_rate(id_ship,manifest_id);
    RETURN occupancy_rate;


EXCEPTION 
    WHEN NO_DATA_FOUND THEN
    manifest_id  := NULL;

END func_occupancy_rate_given_moment;