-- create function to get average occupancy rate
create or replace function func_average_occupancy_rate_period(id_fleet_manager fleet.system_user_code_manager%type,id_ship ship.mmsi%type, start_date date, end_date date)
return number
is
    Number_Manifests number;
    vIdFull cargomanifest_full.id%type;
    totalOccupancyRate number;
    occupancyRate number;

    fleet_manager_id employee.system_user_code_employee%type;
    ship_identification ship.mmsi%type;
    checks integer;

cursor src is (select cf.id
                from cargomanifest_full cf, ship s
                where (select f.id
                        from fleet f
                        where f.system_user_code_manager = id_fleet_manager) = s.fleet_id
                and cf.ship_mmsi = id_ship
                and cf.finishing_date_time between start_date and end_date);
begin

    -- checks if user is valid
    begin
        select system_user_code_employee
        into fleet_manager_id
        from employee
        where role_id = 1
        and system_user_code_employee = id_fleet_manager;

        exception
            when NO_DATA_FOUND then
            RAISE_APPLICATION_ERROR(-20010, 'No such user with the role of fleet manager.');

    end;

    -- checks if ship is valid
    begin
        select mmsi
        into ship_identification
        from ship s
        where s.fleet_id = (select f.id
                            from fleet f
                            where f.system_user_code_manager = id_fleet_manager)
        and s.mmsi = id_ship;
        exception
            when NO_DATA_FOUND then
            RAISE_APPLICATION_ERROR(-20011, 'User does not have access to this ship.');
    end;

    -- checks if start date is bigger than end date
    begin
        if start_date > end_date then
            RAISE_APPLICATION_ERROR(-20012, 'Start date is greater than end date');
        end if;
    end;

    -- function
    begin
        -- gets number of manifests with same ship mmsi
        select count(cf.id)
        into Number_Manifests
        from cargomanifest_full cf, ship s
        where (select f.id
                from fleet f
                where f.system_user_code_manager = id_fleet_manager) = s.fleet_id
        and cf.ship_mmsi = id_ship
        and cf.finishing_date_time between start_date and end_date;

        totalOccupancyRate := 0;

        open src;

        Loop
            Fetch src into vIdFull;
            Exit When src%NOTFOUND;

            -- get occupancy rate of a manifest
            select func_occupancy_rate(id_ship, vIdFull)
            into occupancyRate
            from dual;

            -- totalOccupancyRate += occupancyRate
            totalOccupancyRate := totalOccupancyRate + occupancyRate;

        End Loop;

        Close src;

        if Number_Manifests = 0 then
            return 0;
        else
            return totalOccupancyRate/number_manifests;
        end if;

    exception
        when no_data_found then
            return 0;
    end;
end;
/


-- Call function to test it
select func_average_occupancy_rate_period(6, 100000001, TO_TIMESTAMP('2019-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2022-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) from dual

-- Call function to see if it raises error in case of invalid Fleet Manager
select func_average_occupancy_rate_period(102, 100000001, TO_TIMESTAMP('2019-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2022-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) from dual

-- Call function to see if it raises error in case of ship is invalid for current user
select func_average_occupancy_rate_period(6, 100000002, TO_TIMESTAMP('2019-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2022-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) from dual

-- Call function to see if it raises error in case of starting date is greater than end date
select func_average_occupancy_rate_period(6, 100000001, TO_TIMESTAMP('2100-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2022-05-20 7:59:23', 'YYYY-MM-DD HH24:MI:SS')) from dual