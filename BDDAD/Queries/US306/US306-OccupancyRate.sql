create or replace FUNCTION func_occupancy_rate_storage(id_storage storage.identification%type)
RETURN NUMBER
IS
    Storage_Capacity number(5,2);
    Container_Volume number(4,1);

BEGIN
    SELECT s.MAX_VOLUME, sum(con.MAX_VOLUME)
    INTO Storage_Capacity, Container_Volume
    FROM CONTAINER con, STORAGE s, CARGOMANIFEST_PARTIAL cp, CONTAINER_CARGOMANIFEST cc
    WHERE s.identification = id_storage
    AND cp.storage_identification = s.identification
    AND cp.FINISHING_DATE_TIME IS NOT NULL
    AND LOADING_FLAG = 0 
    AND cc.partial_cargo_manifest_id = cp.id
    AND cc.container_num = con.num
    group by s.MAX_VOLUME;
    RETURN Container_Volume/Storage_Capacity;
END;