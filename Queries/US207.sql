SELECT COUNT(c.id) AS "Transported Cargo Manifest", AVG(count(cc.container_num)) AS "Average Number of containers"
    FROM container_cargoManifest cc, cargomanifest c, ship s, captain cp
    WHERE s.captain_id = 1
    AND s.captain_id = cp.id
    AND c.ship_mmsi = s.mmsi
    AND EXTRACT(YEAR FROM c.finishing_date_time) = 2020
    AND cc.cargo_manifest_id = c.id
    GROUP BY c.id