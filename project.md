# Spring Tribes Project specification

## API specification

https://app.swaggerhub.com/apis-docs/greenfoxacademy/tribes/3.3.1

## Game logic

|        |Building time   ||Building cost       ||HP     |Effect                                                         |
|--------|-------|---------|--------|------------|-------|---------------------------------------------------------------|
|        |Level 1|Level n  |Level 1 |Level n     |Level n|Level n                                                        |
|Townhall|2:00   |n * 1:00 |200 gold|n * 200 gold|n * 200|can build level n buildings                                    |
|Farm    |1:00   |n * 1:00 |100 gold|n * 100 gold|n * 100|+(n * 5) + 5 food / minute                                     |
|Mine    |1:00   |n * 1:00 |100 gold|n * 100 gold|n * 100|+(n * 5) + 5 gold / minute                                     |
|Academy |1:30   |n * 1:00 |150 gold|n * 100 gold|n * 150|can build level n troops                                       |
|Troop   |0:30   |n * 0:30 |25 gold |n * 25 gold |n * 20 |-(n * 5) food / minute<br>+(n * 10) attack<br>+(n * 5) defense |

Initial satus for a Player: 100 gold, 1 Townhall (Lvl 1), 1 Farm (Lvl 1), 1 Mine (Lvl 1), 1 Academy (Lvl 1)

When the Player is created, all its buildings are created as done: the building time is 0.

New buildings do not generate resources until they are finished.

Buildings under upgrade time generate resources based on their starting level (level before upgrade).

The construction time of resources is in seconds, and the provided data indicates only proportionality. If a mine creates 10 gold / minute you get the first gold after 6 seconds. Both gold and food can only take whole values.

When the kingdom runs out of food, all the troops die.

### Logic for battle

From the attacker Kingdom's (AK) troop list, the first randomly selected troop hits the first randomly selected troop from the defender Kingdom's (DK) troop list.

The logic of the hit is as follows:

- If the defense (D) value of the defending troop (DT) is greater than or equal to the attack (A) value of the attacker troop (AT), the HP (health points) value of DT is reduced by 1.
- If the defense (D) value of DT is less than the attack (A) value of AT, the HP (health points) value of DT is modified:
  - DT HP = DT HP - (AT A - DT D).
If DT HP becomes <= 0, DT dies. In this case, the HP value of one randomly selected building in the defending Kingdom decreases by the theoretical value of DT HP that goes below zero.
  - So, if DT HP would go down to -8, the building's HP value decreases by 8.
If the building's HP value goes below 0, the building gets destroyed.
In each hit cycle, we again select which building will be damaged.

If DT survives, it retaliates against AT.
If DT dies, then the next randomly selected troop of DK hits the first troop of AK that initiated the battle.
As a general rule: if the defender survives, it hits back.

The game continues until one of the kingdoms loses all of its troops or its town hall.
The winning kingdom receives all the buildings or troops of the losing kingdom.
