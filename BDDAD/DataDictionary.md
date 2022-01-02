# Naming Conventions

## Delimiting conventions
| **Naming convention** | **Use case** | **Example**
|:-----|:-----|:-----
|**Pascalcase** <br> *VariableExample*|Table names| Ship
|**Snakecase** <br> *variable_example*|Table columns (attributes)| ship_length
|**Camelcase** <br> *variableExample*|Table constraints| ckLength

## Table and attribute naming
**Tables** - entity is defined as a **singular** noun *e.g. Ship* <br>
**Many-to-many relationships** - tables derived from many-to-many relationships are named as **Table1_Table2** e.g. Container_CargoManifest <br>
**Foreign keys** - foreign keys are named as **table1_attribute1** *e.g. ship_mmsi* (a comment/description may be appended at the end of the name, such as *user_id_captain*)

## Terms, Expressions and Acronyms (TEA)

| **_TEA_** (EN)  | **_Description_** (EN) |
|:-----|:------|
|**FK**| Foreign Key|
|**PK**| Primary Key|
|**NN**| Not Null|
|**UN**| Unique|

## Data Glossary

| **Business Concept** | **Description** |
| :---------| :-------|
| **Cargo Manifest** | A document comprised of a list of containers. Ships carry a **full** cargo manifest, listing all containers on board. Ports and warehouses generate **partial** cargo manifests listing containers to be loaded or offloaded a certain ship. |
| **Storage** | Place that stores shipping containers and where cargo loading and unloading happens *e.g. ports, warehouses*. |
| **Check digit** | A digit that is used to detect errors in a shipping container number. |
| **ISO code** | Container marking code to depict the length, height and type of container. |
| **Gross weight** | Weight of a container with cargo included. |
| **Tare weight** | Weight of an empty container. |
| **Payload** | Weight of a container's cargo only. |
| **Container position** | Position of a container on a ship according to a three axis displacement, always starting on the same corner of the ship, the corner between Port and Stern.|
| **CSC plate** | Container Safety Convention. |
| **Fumigation** | Disinfection process of a container.|
| **ACEP** | Container inspection method to ensure container safety.|
| **Racking test** | Test conducted to a container for its maximum load stacking capacity before it starts warping. |
| **MMSI** | Maritime Mobile Service Identity. |
| **IMO** | International Maritime Organization (IMO) - a unique identifier for ships, registered ship owners and management companies. |
| **Callsign** | Unique identifiers to ships and boats used in radio transmissions. |
| **Draft** | Vertical distance between the waterline and the bottom of a ship's hull. |
| **SOG** | Speed over ground. |
| **COG** | Course over ground - direction relative to absolute North. |
| **Transceiver class** | Class to transceiver used when sending data. |
| **Available ship** | A ship that has no trips planned and has no trip in progress. |

## Database Exception Codes
| **Code** | **Exception** | **Message** |
| :------- | : ------------| :-----------|
| -20000 | Ship is in transit, can't add container to cargo manifest | 'Ship is in transit, unable to add container to cargo manifest.' |
| -20001 | Invalid client identifier. | 'Invalid identifier. Client does not exist' |
| -20002 | Invalid container identifier. | 'Error Code: 10 - Invalid identifier - Container no. X' |
| -20003 | Container not leased to client. | 'Error Code: 11 - Invalid access - Container no. X' |
| -20014 | Ship capacity was exceeded. | 'Containers in cargo manifest exceed ship capacity. Please remove containers from manifest or issue an unloading order.' |
| -20015 | Storage capacity was exceeded | 'Containers in cargo manifest exceed storage capacity. Please remove containers from manifest or issue a loading order.' |

## Database Change Log
### Sprint 2 -> Sprint 3

- Added **Country** and **Border** tables.

- Added *country* and *maximum volume* attributes to Storage.

- Added **StoragePath** table to represent connections between storages.

- Added *parting* and *arrival* dates to Shipment table. Added a *client* user code.

- Separated table **CargoManifest** into **Full** and **Partial** tables. Replaced *cargo_manifest* attribute in **Container_CargoManifest** table with *partial_cargo_manifest* and *full_cargo_manifest*.

- Added a *status* attribute to cargo manifests to allow for estimated dates to be inserted.

- Added **Truck** table.

## Database Technology

This database uses **ORACLE SQL**, for the following reasons:

- Possibility to work with complex queries and reports (essential for a management application for a logistics company);

- High transaction application (must be able to store large amounts of data - such as ship positions by the hour/minute/second) in a stable way and ensuring data integrity;

- Rigid business concepts (not a lot of changes to the business model are anticipated since it is a traditional industry);

- No need for many data types.