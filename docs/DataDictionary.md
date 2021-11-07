# Naming Conventions

### Delimiting conventions
| **Naming convention** | **Use case** | **Example**
|:-----|:-----|:-----
|**Pascalcase** <br> *VariableExample*|Table names| Ship
|**Snakecase** <br> *variable_example*|Table columns (attributes)| ship_length
|**Camelcase** <br> *variableExample*|Table constraints| ckLength

### Table and attribute naming
**Tables** - entity is defined as a **singular** noun *e.g. Ship* <br>
**Many-to-many relationships** - tables derived from many-to-many relationships are named as **Table1_Table2** e.g. Container_CargoManifest <br>
**Foreign keys** - foreign keys are named as **table1_attribute1** *e.g. ship_mmsi* (a comment/description may be appended at the end of the name, such as *user_id_captain*)

### Glossary
**Terms, Expressions and Acronyms (TEA)**

| **_TEA_** (EN)  | **_Description_** (EN) |
|:-----|:------|
|**FK**| Foreign Key|
|**PK**| Primary Key|
|**N**| Not Null|
|**U**| Unique|

