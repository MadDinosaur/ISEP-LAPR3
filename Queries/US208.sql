CREATE OR REPLACE FUNCTION occupancy_rate(id_ship ship.mmsi%type, manifest_id cargomanifest.id%type)
RETURN number
IS 
    Ship_Capacity number(5,2);
    Container_Volume number(4,1);

BEGIN
    SELECT (s.capacity), sum(con.max_volume)
    INTO Ship_Capacity, Container_Volume
    FROM cargomanifest c, container_cargoManifest cc, ship s, container con
    WHERE s.mmsi = id_ship
    AND c.id = manifest_id
    AND c.ship_mmsi = s.mmsi
    AND cc.cargo_manifest_id = c.id
    AND cc.container_num = con.num
    GROUP BY s.capacity;
    RETURN Container_Volume/Ship_Capacity;
END;
