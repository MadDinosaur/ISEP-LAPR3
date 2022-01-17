create or replace function func_average_occupancy_rate_period(id_fleet_manager fleet.system_user_code_manager%type,id_ship ship.mmsi%type, start_date date, end_date date)
return number
is
    Number_Manifests number;
    vIdFull cargomanifest_full.id%type;
    totalOccupancyRate number;
    occupancyRate number;

cursor src is (select cf.id
                from cargomanifest_full cf, ship s
                where (select f.id
                        from fleet f
                        where f.system_user_code_manager = id_fleet_manager) = s.fleet_id
                and cf.ship_mmsi = id_ship
                and cf.finishing_date_time between start_date and end_date);

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