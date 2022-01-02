-- Function create script --
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
    AND cp.status like 'finished'
    AND LOADING_FLAG = 0 
    AND cc.partial_cargo_manifest_id = cp.id
    AND cc.container_num = con.num
    AND cp.FINISHING_DATE_TIME = (SELECT MAX(finishing_date_time)
                                    FROM CARGOMANIFEST_PARTIAL
                                    INNER JOIN CONTAINER_CARGOMANIFEST ccc ON cargomanifest_partial.id = ccc.partial_cargo_manifest_id 
                                    WHERE ccc.container_NUM = con.num
                                  )
    group by s.MAX_VOLUME;
    RETURN Container_Volume/Storage_Capacity;
END;

--call function to test it --
select func_occupancy_rate_storage(1) from dual