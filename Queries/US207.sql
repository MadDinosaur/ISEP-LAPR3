Select count(c.id) as "Transported Cargo Manifest", avg(count(cc.container_num)) as "Average Number of containers"
from container_cargoManifest cc, cargomanifest c, ship s, captain cp
where s.captain_id = 1
and s.captain_id = cp.id
and c.ship_mmsi = s.mmsi
and extract(year from c.finishing_date_time) = 2020
and cc.cargo_manifest_id = c.id
group by c.id