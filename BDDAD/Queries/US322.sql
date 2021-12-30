-- define database triggers
CREATE OR REPLACE TRIGGER trgUpdateCargoManifest
    BEFORE UPDATE ON CargoManifest_Partial
    FOR EACH ROW
    WHEN (((old.finishing_date_time IS NULL AND new.finishing_date_time IS NOT NULL)
        OR (new.finishing_date_time <> old.finishing_date_time)
        OR (new.status <> old.status)))
    DECLARE
        vShipCargoManifest_Full CargoManifest_Full%rowtype;
        vStorageCargoManifest_Full CargoManifest_Full%rowtype;
        vContainer Container_CargoManifest%rowtype;
    BEGIN
        IF :new.status LIKE 'finished' THEN
            -- find current full manifest for the storage
            BEGIN
                SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                WHERE storage_identification = :new.storage_identification
                AND FINISHING_DATE_TIME IS NULL;
            EXCEPTION
                -- if full manifest does not exist yet, create it
                WHEN no_data_found THEN
                     INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (:new.storage_identification, NULL);
                     SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                        WHERE storage_identification = :new.storage_identification
                        AND finishing_date_time IS NULL;
            END;
            IF :new.ship_mmsi IS NOT NULL THEN
            -- find current full manifest for the ship
            BEGIN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND FINISHING_DATE_TIME IS NULL;
            EXCEPTION
                -- if full manifest does not exist yet, create it
                WHEN no_data_found THEN
                     INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (:new.ship_mmsi, NULL);
                     SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                        WHERE ship_mmsi = :new.ship_mmsi
                        AND finishing_date_time IS NULL;
            END;
            END IF;

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    -- loading operation
                    IF :new.loading_flag = 1 THEN
                        -- add containers to ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                            VALUES (vContainer.container_num, vShipCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                        END IF;
                        -- remove containers from storage
                        DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id AND container_num = vContainer.container_num;

                    -- unloading operation
                    ELSE
                        -- remove containers from ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id AND container_num = vContainer.container_num;
                        END IF;
                        -- add containers to storage
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vStorageCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    END IF;
                END LOOP;

            -- close modified cargo manifest and create a new current one
            IF :new.ship_mmsi IS NOT NULL THEN
                UPDATE CargoManifest_Full SET finishing_date_time = :new.finishing_date_time, status = 'finished' WHERE id = vShipCargoManifest_Full.id;
                INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (vShipCargoManifest_Full.ship_mmsi, NULL);
                FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id)
                    LOOP
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = vShipCargoManifest_Full.ship_mmsi AND finishing_date_time IS NULL), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                    END LOOP;
            END IF;

            UPDATE CargoManifest_Full SET finishing_date_time = :new.finishing_date_time, status = 'finished' WHERE id = vStorageCargoManifest_Full.id;
            INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (vStorageCargoManifest_Full.storage_identification, NULL);
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE storage_identification = vStorageCargoManifest_Full.storage_identification AND finishing_date_time IS NULL), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;
        ELSE
            -- find last estimate manifest for the ship
            IF :new.ship_mmsi IS NOT NULL THEN
            BEGIN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                WHERE ship_mmsi = :new.ship_mmsi
                AND status LIKE 'pending'
                AND finishing_date_time = (SELECT MAX(cf.finishing_date_time) FROM CargoManifest_Full cf WHERE cf.finishing_date_time < :new.finishing_date_time AND cf.ship_mmsi IS NOT NULL);
            EXCEPTION
                -- if estimate manifest does not exist, get current manifest
                WHEN no_data_found THEN
                    BEGIN
                         SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                         WHERE ship_mmsi = :new.ship_mmsi
                         AND FINISHING_DATE_TIME IS NULL;
                    EXCEPTION
                         WHEN NO_DATA_FOUND THEN
                             INSERT INTO CargoManifest_Full (ship_mmsi, status) VALUES (:new.ship_mmsi, NULL);
                             SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                                WHERE ship_mmsi = :new.ship_mmsi
                                AND finishing_date_time IS NULL;
                    END;
            END;
            END IF;
            -- find last estimate manifest for the storage
            BEGIN
                SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full
                WHERE storage_identification = :new.storage_identification
                AND status LIKE 'pending'
                AND finishing_date_time = (SELECT MAX(cf.finishing_date_time) FROM CargoManifest_Full cf WHERE cf.finishing_date_time < :new.finishing_date_time AND cf.storage_identification IS NOT NULL);
            EXCEPTION
                -- if estimate manifest does not exist, get current manifest
                WHEN no_data_found THEN
                    BEGIN
                         SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                         WHERE storage_identification = :new.storage_identification
                         AND FINISHING_DATE_TIME IS NULL;
                    EXCEPTION
                         WHEN NO_DATA_FOUND THEN
                             INSERT INTO CargoManifest_Full (storage_identification, status) VALUES (:new.storage_identification, NULL);
                             SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full
                                WHERE storage_identification = :new.storage_identification
                                AND finishing_date_time IS NULL;
                    END;
            END;

            -- create new cargo manifest as a copy
            IF :new.ship_mmsi IS NOT NULL THEN
                INSERT INTO CargoManifest_Full (ship_mmsi, finishing_date_time, status) VALUES (:new.ship_mmsi, :new.finishing_date_time, 'pending');
                FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending'), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;
            END IF;

            INSERT INTO CargoManifest_Full (storage_identification, finishing_date_time, status) VALUES (:new.storage_identification, :new.finishing_date_time, 'pending');
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id)
                LOOP
                    INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                    VALUES (vContainer.container_num, (SELECT id FROM CargoManifest_Full WHERE storage_identification = :new.storage_identification AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending'), vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                END LOOP;

            -- update variable
            IF :new.ship_mmsi IS NOT NULL THEN
                SELECT * INTO vShipCargoManifest_Full FROM CargoManifest_Full WHERE ship_mmsi = :new.ship_mmsi AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending';
            END IF;

            SELECT * INTO vStorageCargoManifest_Full FROM CargoManifest_Full WHERE storage_identification = :new.storage_identification AND finishing_date_time = :new.finishing_date_time AND status LIKE 'pending';

            -- find and update containers to be loaded/offloaded
            FOR vContainer IN (SELECT * FROM Container_CargoManifest WHERE partial_cargo_manifest_id = :new.id)
                LOOP
                    -- loading operation
                    IF :new.loading_flag = 1 THEN
                        -- add containers to ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                            VALUES (vContainer.container_num, vShipCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
                        END IF;
                        -- remove containers from storage
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vStorageCargoManifest_Full.id AND container_num = vContainer.container_num;
                    -- unloading operation
                    ELSE
                        -- remove containers from ship
                        IF :new.ship_mmsi IS NOT NULL THEN
                            DELETE FROM Container_CargoManifest WHERE full_cargo_manifest_id = vShipCargoManifest_Full.id AND container_num = vContainer.container_num;
                        END IF;
                        -- add containers to storage
                        INSERT INTO Container_CargoManifest (container_num, full_cargo_manifest_id, container_position_x, container_position_y, container_position_z)
                        VALUES (vContainer.container_num, vStorageCargoManifest_Full.id, vContainer.container_position_x, vContainer.container_position_y, vContainer.container_position_z);
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
