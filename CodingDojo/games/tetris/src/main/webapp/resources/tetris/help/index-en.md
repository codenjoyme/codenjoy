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
found [here](https://github.com/codenjoyme/codenjoy-clients.git)

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

So, the player [registers on the server](../../../register?gameName=tetris)
and joining the game.

Then you should connect from client code to the server via websockets.
This [collection of clients](https://github.com/codenjoyme/codenjoy-clients.git)
for different programming languages will help you. How to start a
client please check at the root of the project in the README.md file.

If you can't find your programming language, you're gonna
have to write your client (and then send us to the mail:
[apofig@gmail.com](mailto:apofig@gmail.com))

Address to connect the game on the server looks like this (you can
copy it from your game room):

`https://[server]/codenjoy-contest/board/player/[user]?code=[code]`

Here `[server]` - domain/id of server, `[user]` is your player id
and `[code]` is your security token. Make sure you keep the code
safe from prying eyes. Any participant, knowing your code, can
play on your behalf.

## Message format

After connection, the client will start getting a string of characters with
the encoded state of the glass in JSON format (every second).

String length of `layers` property equals field area. 
Adding hyphen every `sqrt(length(string))` symbols will give a 
comprehensible view of the field.

## Field example

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

## Symbol breakdown

Please [check it here](elements.md).

## What to do

The game has step-by-step format. Every second server sends to your client (bot)
the status of the updated glass and waits for response.
The player's algorithm must make a decision where to move the figure withing next second.
If the algorithm fails to make this decision - the figure falls one cell down.

Your goal is to make the hero move according to your algorithm. The
algorithm must earn points as much as possible. The ultimate goal is
winning the game.

## Commands

* `LEFT`, `RIGHT` – to move the figure to the left/right.
* `DOWN` - to land the figure.
* `ACT` - to rotate the figure 90 degrees clockwise.
* `ACT(2)` - to rotate the figure 180 degrees.
* `ACT(3)` - to rotate the figure 90 degrees counterclockwise.
* `ACT(0,0)` - zeroing glass (as well as for its overflow penalty points will be taken).
* Movement/rotation/landing commands can be combined by separating them with a comma -
  this means that a given chain of commands will be executed in one tact.

## Points

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

## Cases

## <a id="ask"></a> Ask Sensei

Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Hints

First of all you'll have to choose the programming language.
Next, open WebSocket client in IDE and run it.
Details may be found inside Readme.txt in the root of project.
You'll connect to the server following that instructions.
Next, you'll have to get the falling figures to obey your commands.

## Clients and API

The client code does not give a considerable handicap to gamers because
you should spend time to puzzle out the code. However, it is pleasant
to note that the logic of communication with the server plus some high
level API for working with the board are implemented already.

* `Solver`
  An empty class with one method — you'll have to fill it with smart logic.
* `Direcion`
  Possible commands for this game.
* `Point`
  `x`, `y` coordinates.
* `Element`
  Type of the element on the board.
* `Board` - encapsulating the line with useful methods for searching
  elements on the board.

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy).
All instructions are in Readme.md file, you'll know what to do next once you read it.

If you have any questions reach me in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).

Good luck and may the best win!