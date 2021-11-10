-- DOMAIN RESTRICTIONS
-- Tests if all the columns in the database are restricted to a particular domain.

-- restriction

-- test

-- expected result



-- IDENTITY RESTRICTIONS
-- Tests if the primary key in every table is unique and not null.

--constraint:  pkStorageIdentification PRIMARY KEY,
--test
INSERT INTO Storage VALUES (NULL,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude);
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude) VALUES (1,1,1,'TestStorage','TestContinent','TestCountry',91,181);
INSERT INTO Storage(identification,system_user_id_manager,storage_type_id,name,continent,country,latitude,longitude) VALUES (1,1,1,'TestStorage','TestContinent','TestCountry',91,181);
--expected result: FAIL
--constraint:  pkContainerNum PRIMARY KEY,


-- REFERENTIAL RESTRICTIONS
-- Tests if the foreign key refers to a primary key value of some table in the database.


-- APPLICATION RESTRICTIONS
-- Tests other sets of rules defined by the business model.
