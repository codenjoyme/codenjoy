<meta charset="UTF-8">

## Intro

The game server is available for familiarization reasons
[http://codenjoy.com/codenjoy-contest](http://codenjoy.com/codenjoy-contest).

This is the open source game. To realize your game, correct errors in the current
version and make the other corrections, you should
[fork the project](https://github.com/codenjoyme/codenjoy) at first.
There is the description in the Readme.md file in the repository root.
It is specified in the description what to do next.

If any questions, please write in [skype:alexander.baglay](skype:alexander.baglay)
or Email [apofig@gmail.com](mailto:apofig@gmail.com).

Game project (for writing your bot) can be
found [here](../../../resources/tetris/user/clients.zip)

## What is the game about

Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.

You have to write your own bot which will get more points then other bots
playing Tetris.

Each player has its own board. Player can
move the figure to the left and right, rotate and land it.
Everything works the same way as in classic Tetris, however not all figures
appear from the beginng: once you know how to deal with "squares" -
the sticks will start appearing, and so on untill you open all figures.

The further you go, the more points you earn.
The primary way to score points in Tetris is to clear lines by manipulating
the figures so that they fill horizontal row within the Matrix. As the figures fall,
your goal is to move and spin them so that they line up evenly at the bottom of the Matrix.
To clear a line, every square of the row has to be filled.

## Connect to the server

Player has to [registers on the server](../../../register?gameName=tetris)
using their email address.

Next, player has to connect to the server [from code](../../../resources/tetris/user/clients.zip)
using WebSockets. It's a maven project and it's suitable for playing using JVM languages.
The instruction how to run it may be found at the root of the project in README.txt file.

Other programming languages are also supported - the sources are available in the same archive.

Address to connect the game on the http://codenjoy.com server:

`ws://codenjoy.com:80/codenjoy-contest/ws?user=[user]&code=[code]`

Address to connect the game on the server deployed in the local area network (LAN):

`ws://[server]:8080/codenjoy-contest/ws?user=[user]&code=[code]`

Here `[server]` - ip/domain address of server, `[user]` is your
player id and `[code]` is your security token - you can get
it from browser address bar after registration/login.

## Message format

After connection, the client will start getting a string of characters with
the encoded state of the glass in JSON format (every second).
The format is following:

<pre>{
  'currentFigurePoint':{'x':4,'y':9},
  'currentFigureType':'O',
  'futureFigures':['S', 'Z', 'I', 'O'],
  'layers':[
    '..................
    ........OO........
    ........OO........
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..................
    ..I...............
    ..I......OO.......
    ..IOO..SSOOZZ.....
    ..IOO.SSIIIIZZ....'
  ]
}</pre>

Parameter `currentFigurePoint` contains coordinates of the new figure.
`[0, 0]` - bottom left corner.

Each figure has its owh rotation point.

![](img/rotate.png)

Parameter `currentFigureType` contains the type of the new figure.

Parameter `futureFigures` contains the info about upcomming figures
which will appear after the current one is landed.

Parameter `layers` is an array with the size 1 which contains encoded state of the glass.
The length of `layers[0]` equals to the square area of the field (18 x 18).
If you put a newline character every `sqrt(length(string))` of characters
you'll get a readable view of the glass.

<pre>..................
........OO........
........OO........
..................
..................
..................
..................
..................
..................
..................
..................
..................
..................
..................
..I...............
..I......OO.......
..IOO..SSOOZZ.....
..IOO.SSIIIIZZ....</pre>

![](img/glass.png)

First char of the string corresponds to cell located on the upper left
corner with coordinates `[0, 17]`.

[Interpretation of characters](elements.md)

## Manipulation commands

The game has step-by-step format. Every second server sends to your client (bot)
the status of the updated glass and waits for response.
The player's algorithm must make a decision where to move the figure withing next second.
If the algorithm fails to make this decision - the figure falls one cell down.

There are several commands: 

* `LEFT`, `RIGHT` – to move the figure to the left/right.
* `DOWN` - to land the figure.
* `ACT` - to rotate the figure 90 degrees clockwise.
* `ACT(2)` - to rotate the figure 180 degrees.
* `ACT(3)` - to rotate the figure 90 degrees counterclockwise.
* `ACT(0,0)` - zeroing glass (as well as for its overflow penalty points will be taken).

Movement/rotation/landing commands can be combined by separating them with a comma -
this means that a given chain of commands will be executed in one tact.

## Scoring

There are bonus and penalty points in this game.
For one landed figure you get the number of points equal to its complexity.
The simplest figure is `O` - 1 point, then `I` - 2 points,
`J` - 3 points, `L` - 4 points, `S` - 5 points, `Z` - 6 points, `T` - 7 points.
New figures will appear in the same sequence with every new level (more and more often).

You'll get more bonus points for cleared lines.
Moreover, the more simultaneously cleared lines - the more profitable.
For example, for one cleared line you'll get `10*level` points,
for 2 cleared lines you'll get `30*level` points,
3 simultaneously cleared lines will give you `50*level` points
and 4 simultaneously cleared lines will give you `100*level` points.
Here level - the level you've currently reached.

Do not forget about penalty points!
Each overflowed glass will cost you `10*level` points earned.
You'll lose same amount of points if you reset the glass manually (command `ACT(0,0)`).
The winner will be the one who collects the most points
during limited amount of time (final competition).
Scoring rules as well as rules of determining the winner may change.
Reach out to the host-Sensei for details.

## Client

If [the client](../../../resources/tetris/user/clients.zip)
is written using programming language you're using -
you may have a great possibility to use a higher-level API:
`Board` class - which encapsulates JSON of state,
and `GlassBoard` - which encapsulates tetris glass,
has useful methods for analyzing free and occupied cells in the glass.
`YourSolver` class - empty class with one method `getAnswer(Board board)`.
You'll have to put your intelligent logic there. You can also put
your new method inside `Board` and `GlassBoard` classes.

First of all you'll have to choose the programming language.
Next, open WebSocket client in IDE and run it.
Details may be found inside Readme.txt in the root of project.
You'll connect to the server following that instructions.
Next, you'll have to get the falling figures to obey your commands,
play a smart game and win!

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy).
All instructions are in Readme.md file, you'll know what to do next once you read it.

If you have any questions reach me in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).