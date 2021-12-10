create or replace trigger trig_validate_capacity
    AFTER INSERT OR UPDATE ON container_cargomanifest
    FOR EACH ROW
    
    DECLARE 
    
    var_occupancy number;
    var_shipMMSI number;
    
    ex_error EXCEPTION;

    BEGIN

    SELECT ship_mmsi 
    INTO var_shipMMSI
    FROM cargomanifest c
    WHERE c.id = :new.cargo_manifest_id;
    
    var_occupancy := func_occupancy_rate(var_shipMMSI,:new.cargo_manifest_id);
    

    IF (var_occupancy > 1) THEN
        ROLLBACK;
        RAISE EX_ERROR;
    END IF;

    EXCEPTION
        WHEN EX_ERROR THEN
            RAISE_APPLICATION_ERROR(-20014,'You cannot add this container to the ship');



    END validate_capacity;