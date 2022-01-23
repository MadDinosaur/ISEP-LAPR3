# Integrative Project - LEI - 2021/2022 - 1st Semester, 2nd Year

### Abstract
A cargo shipping company requires a software system to handle their logistics. This company
operates through land and sea, across different continents and has several warehouses spread along the
world.

## Project Software Structure

### Domain Model
![DM](docs/DM.svg)

### Class Diagram
![CD](docs/CD.svg)

### Database Model
![LogicalDataModel](BDDAD/LogicalDataModel.svg)

## Project Scrum Management

- [Bitbucket](https://bitbucket.org/lei-isep/lapr3-2021-g021/src/master/)
- [Jira](https://jira.dei.isep.ipp.pt/secure/RapidBoard.jspa?rapidView=146&projectKey=LAP22G21&view=planning.nodetail&selectedIssue=LAP22G21-44&issueLimit=100)
- [Jenkins](https://jenkins.dei.isep.ipp.pt/job/lapr3-2021-g021/)
- [SonarQube](https://sonarqube.dei.isep.ipp.pt/dashboard?id=lapr3%3Acargo%3Alapr3-2021-g021)

### Burndown Chart - Sprint 1

![BurndownChart](docs/BurndownChart_Sprint1.png)

### Sprint 2
![BurndownChart](docs/BurndownChart_Sprint2.png)

### Sprint 3
![BurndownChart](docs/BurndownChart_Sprint3.png)

### Sprint 4
![BurndownChart](docs/BurndownChart_Sprint4.png)

### User Stories

Below are user story descriptions and links for analysis and design (DM, CD, SD, SSD) diagrams for each one.

|US|Description|
|:---|:--------|
[US101](docs/UserStories/US101)|As a traffic manager, I wish to import ships from a text file into a BST.|
[US102](docs/UserStories/US102)|As a traffic manager I which to search the details of a ship using any of its codes: MMSI, IMO or Call Sign.|
[US103](docs/UserStories/US103)|As a traffic manager I which to have the positional messages temporally organized and associated with each of the ships.|
[US104](docs/UserStories/US104)|As a traffic manager I which to make a Summary of a ship's movements.
[US105](docs/UserStories/US105)|As a traffic manager I which to list for all ships the MMSI, the total number of movements, Travelled Distance and Delta Distance.
[US106](docs/UserStories/US106)|Get the top-N ships with the most kilometres travelled and their average speed (MeanSOG).|
[US107](docs/UserStories/US107)|Return pairs of ships with routes with close departure/arrival coordinates (no more than 5 Kms away) and with different Travelled Distance.
[US108](BDDAD)|As Project Manager, I want the team to develop the data model required to support all the functionality and to fulfill the purpose of the system to develop.
[US109](DataIntegrityRestrictionsTest.sql)|As Project Manager, I want the team to draft an SQL script to test  whether the database verifies all the data integrity restrictions that are required to fulfil the purpose of the system and the business constraints of the UoD.
[US110](DataDictionary.md)|As Project Manager, I want the team to define the naming conventions to apply when defining identifiers or writing SQL or PL/SQL code.
[US111](docs/LoadingValues.sql)|As Project Manager, I want the team to create a SQL script to load the database with a minimum set of data sufficient to carry out data integrity verification and functional testing.
[US201](docs/UserStories/US201)|As a Port manager, I which to import ports from a text file and create a 2D-treewith port locations.|
[US202](docs/UserStories/US202)|As a Traffic manager, I which to find the closest port of a ship given its CallSign,on a certain DateTime.
[US203](BDDAD)|As Project Manager, I want the team to review the relational data model in viewof the new user stories so it can support all the requirements to fulfil the purpose of thesystem being developed.
[US204](docs/UserStories/US204)|As Client, I want to know the current situation of a specific container being used to transport my goods.
[US205](docs/UserStories/US205)|As Ship Captain, I want the list of containers to be offloaded in the next port, including container identifier, type, position, and load.
[US206](docs/UserStories/US206)|As Ship Captain, I want the list of containers to be loaded in the next port, including container identifier, type, and load.
[US207](docs/UserStories/US207)|As Ship Captain, I want to know how many cargo manifests I have transported during a given year and the average number of containers per manifest.
[US208](docs/UserStories/US208)|As Ship Captain, I want to know the occupancy rate (percentage) of a given ship for a given cargo manifest.
[US209](docs/UserStories/US209)|As Ship Captain, I want to know the occupancy rate of a given ship at a given moment.
[US210](docs/UserStories/US210)|As Traffic manager, I need to know which ships will be available on Monday next week and their location.
[US301](docs/UserStories/US301)|As a Traffic manager, I wish to import data from countries, ports, borders and seadists from the database to build a freight network.
[US302](docs/UserStories/US302)|As a Traffic manager I wish to colour the map using as few colours as possible.
[US303](docs/UserStories/US303)|As a Traffic manager I wish to know which places (cities or ports) are closest to all other places (closeness places).
[US304](docs/UserStories/US304)|As Ship Captain, I want to have access to audit trails for a given container of a given cargo manifest.
[US305](docs/UserStories/US305)|As Client, I want to know the route of a specific container I am leasing.
[US306](docs/UserStories/US306)|As Port manager, I want to know the occupancy rate of each warehouse and an estimate of the containers leaving the warehouse during the next 30 days.
[US307](BDDAD/Queries/US307.sql)|As Port manager, I intend to get a warning whenever I issue a cargo manifest destined for a warehouse whose available capacity is insufficient to accommodate the new manifest.
[US308](BDDAD/Queries/US308.sql)|As Traffic manager, I want to have a system that ensures that the number of containers in a manifest does not exceed the ship's available capacity.
[US309](BDDAD/Queries/US309.sql)|As Traffic manager, I do not allow a cargo manifest for a particular trip to be registered in the system on a date when the ship is already in transit.
[US310](docs/UserStories/US310)|As Port manager, I intend to have a map of the occupation of the existing resources in the port during a given month.
[US312](BDDAD/Queries/US312.sql)|As Client, I want to know the current situation of a specific container being used to transport my goods.
[US313](ARQCP/fill3DMatrix.c)|As a Port staff, given a Cargo Manifest, I wish to fill a statically reserved matrix in memory with each container's ID in its respective place.
[US314](ARQCP/freeSpaces.s)|As a Port staff, I wish to know the total number of free/occupied slots in the transport vehicle.
[US315](ARQCP/isContainerHere.s)|As a Port staff, given a position in the transport vehicle, I wish to know if a container is there or not.
[US316](ARQCP/occupiedSlots.s)|As a Port staff, given a set of positions, I wish to know the total number of occupied slots.
[US317](FSIAP/US320)|As Ship Chief Electrical Engineer I want to know what set of materials to use in a container, to operate at temperatures of 7°C.
[US318](FSIAP/US320)|As Ship Chief Electrical Engineer I want to know what set of materials to use in a container, to operate at temperatures of -5 °C.
[US319](docs/UserStories/US319)|As Ship Chief Electrical Engineer I want to know the thermal resistance, for each operating temperature, of each container that must contain at least three different materials in its walls.
[US320](FSIAP/US320)|As Ship Chief Electrical Engineer I intend to present in a summary document, the choice of materials considered for the two types of containers considered, and their thermal resistances.
[US321](docs/UserStories/US321)|As a Human Resources Manager, I want to be able to register users in the system.
[US322](BDDAD/Queries/US322.sql)|As Project Manager, I want the team to automate updates to the ship's cargo manifest at the database level.
[US401](docs/UserStories/US401)|As a Traffic manager I wish to know which ports are more critical (have greater centrality) in this freight network.
[US402](docs/UserStories/US402)|As a Traffic manager I wish to know the shortest path between two locals (city and/or port).
[US403](docs/UserStories/US403)|As a Traffic manager I wish to know the most efficient circuit that starts from a source location and visits the greatest number of other locations once, returning to the starting location and with the shortest total distance.
[US404](docs/UserStories/US404)|As Fleet Manager, I want to know the number of days each ship has been idle since the beginning of the current year.
[US405](docs/UserStories/US405)|As Fleet Manager, I want to know the average occupancy rate per manifest of a given ship during a given period.
[US406](docs/UserStories/US406)|As Fleet Manager, I want to know which ship voyages – place and date of origin and destination – had an occupancy rate below a certain threshold.
[US407](docs/UserStories/US407)|As Port manager, I intend to generate, a week in advance, the loading and unloading map based on ships and trucks load manifests and corresponding travel plans.
[US409](ARQCP/fillDynamicArray.c)|As a Port staff given a Cargo Manifest, I wish to fill a dynamically reserved array in memory with all the container's information in its respective place.
[US410](ARQCP/calculateEnergy.c)|As a Ship Chief Electrical Engineer, given the position of a container, I want to know the amount of needed energy to keep the container at its required temperature.
[US411](ARQCP/enoughEnergy.c)|As a Ship Chief Electrical Engineer, I want to receive an alert when the current energy generation units are not enough to provide energy to all refrigerated containers at once.
[US412](FSIAP/US412)|As Ship Chief Electrical Engineer, we intend to know how much energy to supply, for each container, in a determined trip, with an exterior temperature of 20 ºC, and a travel time of 2h30.
[US413](docs/UserStories/US413)|As Ship Chief Electrical Engineer, the objective is to know the total energy to be supplied to the set of containers in a certain established trip, assuming that all the containers have the same behaviour.
[US414](FSIAP/US412)|As Ship Chief Electrical Engineer, you want to know how much energy to supply to the container cargo, in a voyage (or route), depending on the position of the containers on the ship.
[US415](FSIAP/US412)|As the ship's captain I need to know how many auxiliary power equipment are needed for the voyage, knowing that each one supplies a maximum of 75 KW.
[US416](FSIAP/US412)|As ship's master I intend to submit a summary document, with the following items.
[US417](docs/UserStories/US417)|As the Ship Captain I want the technical team to search for at least three types of ship/vessels that are better suited to the task (e.g., depending on the type of cargo).
[US418](docs/UserStories/US418)|As the Ship Captain I want the determine the unladen center of mass for each vessel (if different) according to its characteristics.
[US419](docs/UserStories/US419)|As the Ship Captain I want to know where to position containers on the vessel, such that the center of mass remains at xx and yy, determined in the previous point.
[US420](docs/UserStories/US420)|As the Ship Captain I want to know for a specific vessel, how much did the vessel sink, assuming that each container has half a ton of mass.

### Physics
  
To accommodate the design choice made to represent the user stories of physics a little method was made in which the following materials are registered: 
    
    - Stone Wool
    - Cork
    - Fiber-glass
    - Steel
    - Iron
    - Zinc
    - Aluminium

### Tests

Demo tests can be found on *src\test\java\lapr\project\presentationTests*, which output a file with the results of each user story.