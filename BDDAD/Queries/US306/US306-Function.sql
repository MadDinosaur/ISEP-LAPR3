CREATE OR REPLACE FUNCTION func_occupancy_rate_storage(id_storage storage.identification%type)
RETURN NUMBER
IS
    Warehouse_Capacity number(5,2);
    Container_Volume number(4,1);
    
BEGIN
    SELECT (s.max_volume), sum(con.max_volume)
    INTO Warehouse_Capacity, Container_Volume
    FROM cargomanifest_partial c, storage s, container con, container_cargomanifest cc
    WHERE s.identification = id_storage
    AND c.storage_identification = s.identification
    AND cc.partial_cargo_manifest_id = c.id
    AND cc.container_num = con.num
    GROUP BY s.max_volume;
    RETURN Container_Volume/Warehouse_Capacity;
END;