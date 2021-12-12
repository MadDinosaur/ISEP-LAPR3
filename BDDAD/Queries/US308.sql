create or replace trigger trig_validate_capacity
    AFTER INSERT OR UPDATE ON container_cargomanifest
    FOR EACH ROW

    DECLARE
    
    PRAGMA AUTONOMOUS_TRANSACTION;
    var_occupancy number;
    var_shipMMSI number;
    ex_error EXCEPTION;

    BEGIN

    SELECT ship_mmsi 
    INTO var_shipMMSI
    FROM cargomanifest_partial c
    WHERE c.id = :new.full_cargo_manifest_id;

    dbms_output.put_line('olá');
    var_occupancy := func_occupancy_rate(var_shipMMSI,:new.full_cargo_manifest_id);
    dbms_output.put_line(var_occupancy);

    IF (var_occupancy > 1) THEN
        ROLLBACK;
        RAISE EX_ERROR;
    END IF;

    EXCEPTION
        WHEN EX_ERROR THEN
            RAISE_APPLICATION_ERROR(-20014,'You cannot add this container to the ship' || var_occupancy );

    END trig_validate_capacity;

    
    
    SELECT func_occupancy_rate(100000001,1) FROM DUAL;
    
    SELECT* FROM CONTAINER_CARGOMANIFEST WHERE FULL_CARGO_MANIFEST_ID = 1 order by container_num;
    
    INSERT INTO Container_CargoManifest(container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
    VALUES(11, 1, 4,4,3);
    
    DELETE FROM Container_CargoManifest WHERE container_num = 11 AND full_cargo_manifest_id = 1;