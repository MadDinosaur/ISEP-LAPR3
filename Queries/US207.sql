CREATE OR REPLACE PROCEDURE avg_manifest(cap_id captain.id%type, moment integer)
AS
    number_manifest integer;
    avg_manifest number(4, 3);
BEGIN
    SELECT COUNT(c.id), AVG(count(cc.container_num))
    INTO number_manifest, avg_manifest
    FROM container_cargoManifest cc, cargomanifest c, ship s, captain cp
         WHERE s.captain_id = cap_id
         AND s.captain_id = cp.id
         AND c.ship_mmsi = s.mmsi
         AND EXTRACT(YEAR FROM c.finishing_date_time) = moment
         AND cc.cargo_manifest_id = c.id
         GROUP BY c.id;

    IF number_manifest > 0 THEN
        dbms_output.put_line('Transported Cargo Manifest - Average Number of containers');
        dbms_output.put_line(number_manifest  || ' - ' || avg_manifest);
    ELSE
        dbms_output.put_linenumber_mani('No manifests where transported this year');
    END IF;
END;