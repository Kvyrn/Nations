Players
-------
| Name                  | Data type     |
|-----------------------|---------------|
| Uuid *(key)*          | CHAR(36)      |
| Name                  | TINYTEXT      |
| Playtime              | BIGINT        |
| Nation **(fkey)**     | TINYTEXT      |
| ResourcePoints        | INT           |
| SelectedAbility       | TINYTEXT      |

PlayerAbilities
---------------
| Name                  | Data type     |
|-----------------------|---------------|
| PlayerUuid **(fkey)** | CHAR(36)      |
| AbilityId             | TINYTEXT      |
| Favourite             | BOOL          |
| Cooldown              | INT           |

Nations
-------
| Name                  | Data type     |
|-----------------------|---------------|
| Name *(key*)          | TINYTEXT      |

__________________________________________
**fkey** = *foreign key*