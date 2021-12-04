Players
-------
| Name              | Data type    |
|-------------------|--------------|
| Uuid *(key)*      | CHAR(36)     |
| Name              | TINYTEXT     |
| Playtime          | BIGINT       |
| Nation **(fkey)** | VARCHAR(255) |
| ResourcePoints    | INT          |
| SelectedAbility   | TINYTEXT     |

PlayerAbilities
---------------
| Name                  | Data type |
|-----------------------|-----------|
| PlayerUuid **(fkey)** | CHAR(36)  |
| AbilityId             | TINYTEXT  |
| Favourite             | BOOL      |
| Cooldown              | INT       |

Nations
-------
| Name         | Data type    |
|--------------|--------------|
| Name *(key*) | VARCHAR(255) |

__________________________________________
**fkey** = *foreign key*