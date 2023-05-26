Important disclaimer. Not all games meet the rules described here. But fixes 
are made, and sooner or later it will happen to all games.

Each game is a separate maven project with a separate repository. All games 
inherit `./games/pom.xml` as their parent configure file. All games use as 
dependency the `./games/engine` module, which in itself depends on the 
`./clients/java` module.

The `engine` module contains all the basic classes and interfaces extracted 
from many games to avoid duplication. There are many of them, and they will be 
described separately below.

The `clients/java` module contains all the basic classes needed to participate 
in the game. The participant connects via web-socket from the client containing 
the base classes to write the AI algorithm. They are in this module at once 
for all games. Namely:
1. Element - enum with a list of options for displaying objects on the field 
in the text representation.
2. Board - a class that allows parsing the text representation of the field 
and providing high-level access methods.
3. YourSolver - the player`s AI itself.

All games are divided into two types: graphical and text-based. The game 
templates are the game (module) `./games/sample` for graphical games and 
`./games/sample-text` for text ones. All architectural changes start with 
these games first, and then spread out over all the others in order: `sample`, 
`mollymage`, `clifford`, `verland`, `rawelbbub`, `namdreab`. So far, these 
are the games where all the architectural innovations have been implemented.

The structure of the project is shown in the picture below.

![sample-structure.png](sample-structure.png)

TODO continue here