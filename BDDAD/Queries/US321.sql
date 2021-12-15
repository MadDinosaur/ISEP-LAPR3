CREATE SEQUENCE user_sequence;

CREATE OR REPLACE TRIGGER user_gen_code
    BEFORE INSERT ON systemuser
    FOR EACH ROW
BEGIN
   SELECT TO_CHAR(user_sequence.nextval)
   INTO :new.registration_code
   FROM dual;
END;
