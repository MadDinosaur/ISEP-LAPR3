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
![LogicalDataModel](docs/LogicalDataModel.svg)

## Project Scrum Management

- [Bitbucket](https://bitbucket.org/lei-isep/lapr3-2021-g021/src/master/)
- [Jira](https://jira.dei.isep.ipp.pt/secure/RapidBoard.jspa?rapidView=146&projectKey=LAP22G21&view=planning.nodetail&selectedIssue=LAP22G21-44&issueLimit=100)
- [Jenkins](https://jenkins.dei.isep.ipp.pt/job/lapr3-2021-g021/)
- [SonarQube](https://sonarqube.dei.isep.ipp.pt/dashboard?id=lapr3%3Acargo%3Alapr3-2021-g021)

### Burndown Chart - Sprint 1

![BurndownChart](docs/BurndownChart.png)

### User Stories

Below are user story descriptions and links for analysis and design (DM, CD, SD, SSD) diagrams for each one.

|US|Description| |
|:--|:--------|:------|
[US101](docs/UserStories/US101)|As a traffic manager, I wish to import ships from a text file into a BST.|
[US102](docs/UserStories/US102)|As a traffic manager I which to search the details of a ship using any of its codes: MMSI, IMO or Call Sign.|
[US103](docs/UserStories/US103)|As a traffic manager I which to have the positional messages temporally organized and associated with each of the ships.|
[US104](docs/UserStories/US104)|As a traffic manager I which to make a Summary of a ship's movements.
[US105](docs/UserStories/US105)|As a traffic manager I which to list for all ships the MMSI, the total number of movements, Travelled Distance and Delta Distance.
[US106](docs/UserStories/US106)|Get the top-N ships with the most kilometres travelled and their average speed (MeanSOG).|
[US107](docs/UserStories/US107)|Return pairs of ships with routes with close departure/arrival coordinates (no more than 5 Kms away) and with different Travelled Distance.
[US108](docs)|As Project Manager, I want the team to develop the data model required to support all the functionality and to fulfill the purpose of the system to develop.
[US109](DataIntegrityRestrictionsTest.sql)|As Project Manager, I want the team to draft an SQL script to test  whether the database verifies all the data integrity restrictions that are required to fulfil the purpose of the system and the business constraints of the UoD.
[US110](DataDictionary.md)|As Project Manager, I want the team to define the naming conventions to apply when defining identifiers or writing SQL or PL/SQL code.
US111|As Project Manager, I want the team to create a SQL script to load the database with a minimum set of data sufficient to carry out data integrity verification and functional testing

### Tests

Demo tests can be found on *src\test\java\lapr\project\presentationTests*, which output a file with the results of each user story.