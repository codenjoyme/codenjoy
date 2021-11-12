<met<meta charset="UTF-8">

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
found [here](../../../resources/snake/user/clients.zip)

## What is the game about

Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.

You must create a custom bot for the snake to beat all the other bots
by gathering the most points. Each player is playing on their own field. The snake can move
to blank cells in all four directions.

On its way, the snake can encounter an apple, a stone, its own tail and a wall.
If the snake eats an apple, it grows longer by 1. If it hits a stone,                            it shrinks by 10 cells. If it is shorter than 10 cells and it hits a stone, it dies.
If the snake runs into itself or a wall, it dies.

Each eaten apple gives the player bonus points 
equal to the size of the snake at the time when the apple is eaten. 
Points may be deduced for letting the snake die (by default, the value is 0, confirm this rule with the event organizer).
Points are cumulative. The winner is the player with the most points (within the predefined
time limit). A dead snake disappears immediately and a new one appears on the field.

## Connect to the server

Player has to [registers on the server](../../../register?gameName=snake)
using their email address.

After that, they must connect [from code](../../../resources/snake/user/clients.zip)
to the server via websockets. This is a Maven project and it is suitable for playing using JVM languages.                            For instructions on how to launch it, see README.txt in the root of the project.
It also contains all supported languages.

If a specific language is absent from the list, you'll have to write your own client (and then share it with us! Here: apofig@gmail.com)

Address to connect the game on the http://codenjoy.com server:

`ws://codenjoy.com:80/codenjoy-contest/ws?user=[user]&code=[code]`

Address to connect the game on the server deployed in the local area network (LAN):

`ws://[server]:8080/codenjoy-contest/ws?user=[user]&code=[code]`

Here `[server]` - ip/domain address of server, `[user]` is your
player id and `[code]` is your security token - you can get
it from browser address bar after registration/login.

## Message format

After connecting, the client will receive a regular (every second) line
of symbols which are the encoded value of the field. Format:

`^board=(.*)$`

Using this regexp you can bite out the line of the board.
Example of a line from the server:

## Field example

<pre>board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼             ☼☼      ☻      ☼☼            ☺☼☼            ▲☼☼            ║☼☼            ║☼☼            ║☼☼            ╙☼☼             ☼☼             ☼☼             ☼☼             ☼☼             ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

Line length equals the area of the field. Inserting a line break symbol                            each `sqrt(length(string))` of symbols will produce a readable
depiction of the field.

<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼             ☼
☼      ☻      ☼
☼            ☺☼
☼            ▲☼
☼            ║☼
☼            ║☼
☼            ║☼
☼            ╙☼
☼             ☼
☼             ☼
☼             ☼
☼             ☼
☼             ☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The first line symbol corresponds with the cell located in the bottom left corner and has the coordinates [0, 0]. In this example, this is the position of the snake's head
(symbol ▲) — [13, 10], and stone (symbol ☻) - [7, 12].

[Interpretation of characters](elements.md)

If the client is made using your chosen software development language,
you will have a chance to use a more high-level API: 
Board — encapsulating the line with useful methods for searching elements on the board, and
YourSolver — an empty class with one method — you'll have to fill it with smart logic.

The game is a step-by-step process which has the server send its client (bot)
the present state of the updated field, and then wait for the response of the command for the snake.
Within the next second the player much give a command to the snake.
If they don't manage to give one in time, the snake will move automatically (until it finds a wall).

There are several commands: UP, DOWN, LEFT, RIGHT which lead to turning the snake's head
in the required direction. I would like to remind you that the snake moves mechanically under its own inertia.

First, make the snake listen to your commands. If there is no client for your language, 
it becomes slightly more complicated — you will have to write a WebSocket for the client. 
Thus, you prepare for the main game. Your next job is to write a bot 
which will follow a specific strategy on the field and will win.