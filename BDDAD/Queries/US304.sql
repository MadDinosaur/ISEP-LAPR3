CREATE TABLE Container_AuditTrail
(
    user_id           VARCHAR2(20)
    CONSTRAINT        nnUserId        NOT NULL,
    date_time         TIMESTAMP
    CONSTRAINT        nnDateTime      NOT NULL,
    operation_type    VARCHAR(6)
    CONSTRAINT        nnOperationType NOT NULL,
    container_num     INTEGER
        CONSTRAINT nnAuditTrailContainerNum NOT NULL,
    cargo_manifest_id INTEGER
        CONSTRAINT nnAuditTrailCargoManifestId NOT NULL,
    CONSTRAINT fkContainerAuditTrailContainerNum FOREIGN KEY (container_num) REFERENCES Container (num),
    CONSTRAINT fkContainerAuditTrailCargoManifestId FOREIGN KEY (cargo_manifest_id) REFERENCES CargoManifest (id),
    CONSTRAINT pkContainerAuditTrail PRIMARY KEY (date_time, container_num, cargo_manifest_id)
);

CREATE OR REPLACE TRIGGER trgContainerAudit
AFTER INSERT OR UPDATE OR DELETE ON CONTAINER_CARGOMANIFEST
FOR EACH ROW
    DECLARE
        vOperationType Container_AuditTrail.operation_type%type;
        vContainerNum Container_AuditTrail.container_num%type;
        vCargoManifestId Container_AuditTrail.cargo_manifest_id%type;
    BEGIN
        IF DELETING THEN
            vContainerNum := :old.CONTAINER_NUM;
            vCargoManifestId := :old.CARGO_MANIFEST_ID;
            vOperationType := 'DELETE';
        ELSE
            vContainerNum := :new.CONTAINER_NUM;
            vCargoManifestId := :new.CARGO_MANIFEST_ID;
            IF INSERTING THEN
                vOperationType := 'INSERT';
            ELSE
                vOperationType := 'UPDATE';
           END IF;
        END IF;
        INSERT INTO Container_AuditTrail VALUES (USER, CURRENT_TIMESTAMP, vOperationType, vContainerNum, vCargoManifestId);
    END;
/

ALTER TRIGGER trgContainerAudit ENABLE;
/