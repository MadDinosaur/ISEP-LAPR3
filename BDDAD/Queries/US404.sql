CREATE OR REPLACE PROCEDURE proc_iddle_days_fleet(fleetID fleet.system_user_code_manager%type)
AS
    vIddleDays NUMBER;
BEGIN

    FOR ship IN(SELECT * FROM SHIP WHERE fleet_id = (SELECT id FROM FLEET WHERE system_user_code_manager = fleetID))
    LOOP
        vIddleDays := FUNC_IDDLE_DAYS(ship.mmsi);
        DBMS_OUTPUT.PUT_LINE('The ship with the MMSI: ' || ship.mmsi || ' has been iddle for  ' || vIddleDays );
    END LOOP;

    EXCEPTION
    WHEN NO_DATA_FOUND THEN
        raise_application_error(-20050,'No ships found for that fleet manager ID');
END;

create or replace FUNCTION FUNC_IDDLE_DAYS(shipMMSI ship.mmsi%TYPE)
RETURN NUMBER
AS 
    vShipTrips SYS_REFCURSOR;
    vWorkingDays NUMBER := 0;
    vIddleDays NUMBER;
    vPartingDate SHIPTRIP.parting_date%TYPE;
    vArrivalDate SHIPTRIP.arrival_date%TYPE;
    vStatus SHIPTRIP.status%TYPE;
    vLastArrivalDate SHIPTRIP.arrival_date%TYPE;
BEGIN
    -- Pega em todos os ShipTrips para o barco dado
   OPEN vShipTrips FOR SELECT   st.arrival_date,st.parting_date,st.status 
                                FROM SHIPTRIP st
                                WHERE (EXTRACT(YEAR FROM st.parting_date) = EXTRACT(YEAR FROM CURRENT_DATE))
                                AND st.ship_mmsi = shipMMSI
                                AND st.status NOT LIKE 'not started'
                                ORDER BY st.parting_date;
    LOOP
        FETCH vShipTrips INTO vArrivalDate,vPartingDate,vStatus;
        EXIT WHEN vShipTrips%NOTFOUND;
        IF(vStatus LIKE 'in progress') THEN                 -- Se o status estiver em Progress então a data limite é a data atual

            vWorkingDays := vWorkingDays + FUNC_DAYS_BETWEEN_TIMESTAMPS(vPartingDate,CURRENT_TIMESTAMP);
        ELSE
            vWorkingDays := vWorkingDays + FUNC_DAYS_BETWEEN_TIMESTAMPS(vPartingDate,vArrivalDate);
        END IF;

        -- Verifica se o dia em questão já foi adicionado anteriormente
        IF(EXTRACT(DAY FROM vLastArrivalDate) = EXTRACT(DAY FROM vPartingDate) AND (EXTRACT(MONTH FROM vLastArrivalDate) = EXTRACT(MONTH FROM vPartingDate))) THEN
            vWorkingDays := vWorkingDays -1;
        END IF;

        IF(EXTRACT(DAY FROM vLastArrivalDate) = EXTRACT(DAY FROM vArrivalDate) AND (EXTRACT(MONTH FROM vLastArrivalDate) = EXTRACT(MONTH FROM vArrivalDate))) THEN
            vWorkingDays := vWorkingDays -1;
        END IF;

        vLastArrivalDate := vArrivalDate; -- Guarda a data anterior que vai servir para fazer a verficação acima referiada
    END LOOP;

    vIddleDays := FUNC_DAYS_SINCE_NEWYEAR(CURRENT_TIMESTAMP)- vworkingdays;

    RETURN vIddleDays;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            raise_application_error(-20049,'Could not find any Shiptrips for this ship');
            RETURN NULL;
END;

create or replace FUNCTION FUNC_DAYS_BETWEEN_TIMESTAMPS(oldestDate TIMESTAMP,recentDate TIMESTAMP) -- Função auxiliar para descobrir quantos dias entre duas datas
RETURN INTEGER
AS
    vResult INTEGER := 1;
    vRecentDate TIMESTAMP := recentDate;
    vOldestDate TIMESTAMP := oldestDate;
BEGIN

    WHILE((EXTRACT(DAY FROM vOldestDate) != EXTRACT(DAY FROM vRecentDate)) OR (EXTRACT(MONTH FROM vOldestDate) != EXTRACT(MONTH FROM vRecentDate)))
    LOOP
        vResult := vResult + 1;
        vOldestDate := vOldestDate + 1;

    END LOOP;

    RETURN vResult;
END;

create or replace FUNCTION FUNC_DAYS_SINCE_NEWYEAR(date TIMESTAMP) -- Função auxiliar para descobrir quantos dias se passaram desde o começo do ano
RETURN NUMBER
AS
    vResult NUMBER := 0;
    vDate TIMESTAMP := date;
BEGIN

    WHILE(EXTRACT(YEAR FROM vDate)) = EXTRACT(YEAR FROM date)
    LOOP
        vResult := vResult + 1;
        vDate := vDate - 1;
    END LOOP;
    RETURN vResult;
END;


--TESTAR--

SET SERVEROUTPUT ON;
exec PROC_IDDLE_DAYS_FLEET(7); -- Testar a User Story na totalidade

SELECT FUNC_IDDLE_DAYS(100000002) FROM DUAL;    -- Testar a função para descubrir Iddle Days

SELECT FUNC_DAYS_BETWEEN_TIMESTAMPS('22.01.01 21:00:00,000000000','22.01.03 12:00:00,000000000') FROM DUAL; -- Função auxiliar para descobrir quantos dias entre duas datas

SELECT FUNC_DAYS_SINCE_NEWYEAR(CURRENT_DATE) FROM DUAL; -- Função auxiliar para descobrir quantos dias se passaram desde o começo do ano