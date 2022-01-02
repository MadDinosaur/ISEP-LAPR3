-- Function create script --
CREATE OR REPLACE FUNCTION func_estimate_number_leaving_containers(id_storage storage.identification%type)
RETURN INTEGER
IS
    Leaving_Containers integer(7);
    
BEGIN
    select count(container_num)
    INTO Leaving_Containers
    FROM STORAGE s, CARGOMANIFEST_PARTIAL cp, container_cargomanifest cc
    where s.identification = id_storage
    and s.identification = cp.storage_identification
    and cp.id = cc.partial_cargo_manifest_id
    and status like 'pending' 
    and cp.finishing_date_time between current_timestamp and current_timestamp + 30;
    RETURN Leaving_Containers;
END;

--call function to test it --
select func_estimate_number_leaving_containers(2) from dual