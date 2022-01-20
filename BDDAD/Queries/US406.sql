CREATE OR REPLACE PROCEDURE trips_bellow_threshold (manager_id SystemUser.registration_code%type)
as
    valid_fleet_id fleet.id%Type;
    fleet_manager_id employee.system_user_code_employee%Type;
    occupancy_rate number;
    number_trips number;
BEGIN
    --Validate User
    BEGIN
        SELECT SYSTEM_USER_CODE_EMPLOYEE INTO fleet_manager_id
        FROM employee
        WHERE role_id = 1
        AND SYSTEM_USER_CODE_EMPLOYEE = manager_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20010, 'No such user with the role of fleet manager.');
            RETURN;
    END;

    --Validate fleet
    BEGIN
        SELECT id INTO valid_fleet_id
        FROM fleet
        WHERE SYSTEM_USER_CODE_MANAGER = fleet_manager_id;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20011, 'User is not assigned to any fleet');
            RETURN;
    END;

    --Procedure
    BEGIN
        SELECT COUNT(*) INTO number_trips  FROM shiptrip
        WHERE status LIKE 'finished' AND ship_mmsi IN
        (SELECT mmsi FROM ship WHERE fleet_id = valid_fleet_id);

        IF number_trips = 0
            THEN
                DBMS_OUTPUT.PUT_LINE('No finished trips found for specified fleet');
            ELSE
                DBMS_OUTPUT.PUT_LINE('Ship  |  Place of origin  |  Parting date  |  Place of destination  |  Parting date  |  Occupancy Rate');
                FOR trip IN(SELECT * FROM shiptrip WHERE status LIKE 'finished' AND ship_mmsi IN (SELECT mmsi FROM ship WHERE fleet_id = valid_fleet_id) ORDER BY parting_date)
                LOOP
                    -- Calls US405 and uses it's function to search for the latest full cargo manifest to get the ship occupancy rate for this trip
                    SELECT ROUND(func_average_occupancy_rate_period(fleet_manager_id, trip.ship_mmsi, trip.parting_date, trip.arrival_date)* 100, 2)
                    INTO occupancy_rate FROM dual;

                    IF occupancy_rate < 66
                        THEN DBMS_OUTPUT.PUT_LINE(trip.ship_mmsi || '  |  ' || trip.STORAGE_IDENTIFICATION_ORIGIN || '  |  ' || trip.PARTING_DATE || '  |  ' || trip.STORAGE_IDENTIFICATION_DESTINATION || '  |  ' || trip.ARRIVAL_DATE || '  |  ' || occupancy_rate || '%');
                    END IF;
                END LOOP;
        END IF;
    END;
END;
/

-- Should fail because user is not a fleet manager
call trips_bellow_threshold(3);

-- Should fail because manager is not assigned to any fleet
call trips_bellow_threshold(13);

-- Should fail because user does not exist
call trips_bellow_threshold(20);

-- Example of working fleet manager
call trips_bellow_threshold(6);

-- Example of working fleet manager except theres no trips completed for this one
call trips_bellow_threshold(8);