CREATE OR REPLACE PROCEDURE check_container_shipment (pClientId SystemUser.registration_code%type, pContainerNum Container.num%type)
AS
    vValidContainer CONTAINER%rowtype;
    vValidClient SYSTEMUSER%rowtype;
    vValidShipment SHIPMENT%rowtype;
BEGIN
    -- Validate client identifier
    BEGIN
        SELECT * INTO vValidClient
        FROM SYSTEMUSER
        WHERE REGISTRATION_CODE = pClientId
        AND (SELECT NAME FROM ROLE WHERE ID = SYSTEMUSER.ROLE_ID) LIKE 'Client';
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20001, 'Invalid identifier. Client does not exist');
            RETURN;
    END;
    -- Validate container identifier
    BEGIN
        SELECT * INTO vValidContainer
        FROM CONTAINER
        WHERE NUM = pContainerNum;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20002, 'Error Code: 10 - Invalid identifier - Container no. ' || pContainerNum || ' does not exist');
            RETURN;
    END;
    -- Validate container leased by client
    BEGIN
        SELECT * INTO vValidShipment
        FROM SHIPMENT
        WHERE SYSTEM_USER_CODE_CLIENT = pClientId
        AND CONTAINER_NUM = pContainerNum;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20003, 'Error Code: 11 - Invalid access - Container no. ' || pContainerNum || ' is not leased by client');
    END;
END;

-- Invalid container (throws exception)
SELECT * FROM CONTAINER;
CALL check_container_shipment('6', -1);

-- Invalid client (throws exception)
SELECT * FROM SYSTEMUSER INNER JOIN ROLE R on R.ID = SYSTEMUSER.ROLE_ID;
CALL check_container_shipment('-1', 1);
CALL check_container_shipment('1', 1);

-- Container not leased (throw exception)
SELECT * FROM SHIPMENT;
CALL check_container_shipment('6', 6);

-- Valid data (does not throw exception)
CALL check_container_shipment('6', 1);