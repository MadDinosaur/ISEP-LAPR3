CREATE OR REPLACE TRIGGER tgrValidateCapacity
    AFTER INSERT ON container_cargomanifest
    FOR EACH ROW

    DECLARE
        varOccupancyNumber number;
        varShipMMSI number;
        varShipCapacity number;
        varContainerVolume number;

        ex_error EXCEPTION;

    BEGIN
     -- finds ship MMSI
        BEGIN
            SELECT ship_mmsi
            INTO varShipMMSI
            FROM cargomanifest_partial cp
            WHERE cp.id = :new.partial_cargo_manifest_id;
        END;

        BEGIN
            SELECT (s.capacity), sum(con.max_volume)
            INTO varShipCapacity, varContainerVolume
            FROM cargomanifest_partial c, container_cargoManifest cc, ship s, container con
            WHERE s.mmsi = varShipMMSI
            AND c.id = :new.full_cargo_manifest_id
            AND c.ship_mmsi = s.mmsi
            AND cc.full_cargo_manifest_id = c.id
            AND cc.container_num = con.num
            GROUP BY s.capacity;
        END;

        varOccupancyNumber := varContainerVolume/varShipCapacity;

         IF (varOccupancyNumber > 1) THEN
            ROLLBACK;
            RAISE EX_ERROR;
            END IF;

         EXCEPTION
            WHEN EX_ERROR THEN
            RAISE_APPLICATION_ERROR(-20014,'You cannot add this container to the ship');

    END trgValidateCapacity;