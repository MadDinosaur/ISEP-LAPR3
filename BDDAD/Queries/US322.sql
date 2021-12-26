CREATE OR REPLACE TRIGGER trgUpdateCargoManifest
    AFTER UPDATE OR INSERT ON CargoManifest_Partial
    FOR EACH ROW
    WHEN (((old.finishing_date_time IS NULL AND new.finishing_date_time IS NOT NULL)
        OR (new.finishing_date_time <> old.finishing_date_time)
        OR (new.status <> old.status))
        AND new.ship_mmsi IS NOT NULL)
    DECLARE
        vCargoManifest_Full CargoManifest_Full%rowtype;
        vContainer Container_CargoManifest%rowtype;
    BEGIN
        IF :new.status LIKE 'finished' THEN
            -- find current full manifest for the ship
            BEGIN
                SELECT * INTO vCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND FINISHING_DATE_TIME IS NULL;
            EXCEPTION
                -- if full manifest does not exist yet, create it
                WHEN no_data_found THEN
                     INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (:new.ship_mmsi, NULL);
                     SELECT * INTO vCargoManifest_Full FROM CargoManifest_Full
                        WHERE ship_mmsi = :new.ship_mmsi
                        AND finishing_date_time IS NULL;
            END;

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    IF :new.loading_flag = 1 THEN
                        -- loading operation
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    ELSE
                        -- unloading operation
                        DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vCargoManifest_Full.id AND container_num = vContainer.container_num;
                    END IF;
                END LOOP;

            -- close modified cargo manifest and create a new current one
            UPDATE CargoManifest_Full SET finishing_date_time = :new.finishing_date_time, status = 'finished' WHERE id = vCargoManifest_Full.id;

            INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (vCargoManifest_Full.ship_mmsi, NULL);

            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = vCargoManifest_Full.ship_mmsi AND finishing_date_time IS NULL), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;
        ELSE
            -- find last estimate manifest for the ship
            BEGIN
                SELECT * INTO vCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND status LIKE 'pending'
                AND finishing_date_time = (SELECT MAX(cf.finishing_date_time) FROM CargoManifest_Full cf WHERE cf.finishing_date_time < :new.finishing_date_time);
            EXCEPTION
                -- if estimate manifest does not exist, get current manifest
                WHEN no_data_found THEN
                     SELECT * INTO vCargoManifest_Full FROM CargoManifest_Full
                     WHERE ship_mmsi = :new.ship_mmsi
                     AND FINISHING_DATE_TIME IS NULL;
            END;

            -- create new cargo manifest as a copy
            INSERT INTO CargoManifest_Full (ship_mmsi, finishing_date_time, status) VALUES (:new.ship_mmsi, :new.finishing_date_time, 'pending');

            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending'), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;

            -- update variable
            SELECT * INTO vCargoManifest_Full FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending';

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    IF :new.loading_flag = 1 THEN
                        -- loading operation
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    ELSE
                        -- unloading operation
                        DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vCargoManifest_Full.id AND container_num = vContainer.container_num;
                    END IF;
                END LOOP;
        END IF;
    END;
/
ALTER TRIGGER trgUpdateCargoManifest ENABLE;
/
-- Tests
SELECT * FROM CONTAINER_CARGOMANIFEST WHERE FULL_CARGO_MANIFEST_ID = 8;

INSERT INTO CargoManifest_Partial(ship_mmsi, loading_flag, storage_identification)
VALUES(100000001, 1, 2);

INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(3, 10, 1,2,2);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(8, 10, 2,1,1);
INSERT INTO Container_CargoManifest(container_num, partial_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
VALUES(9, 10, 1,1,2);

UPDATE CargoManifest_Partial SET finishing_date_time = CURRENT_TIMESTAMP WHERE id = 10;

SELECT * FROM CONTAINER_CARGOMANIFEST WHERE FULL_CARGO_MANIFEST_ID = 8;
