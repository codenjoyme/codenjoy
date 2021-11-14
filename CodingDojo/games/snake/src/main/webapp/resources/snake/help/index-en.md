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
found [here](https://github.com/codenjoyme/codenjoy-clients.git)

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

So, the player [registers on the server](../../../register?gameName=snake)
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

After connecting, the client will receive a regular (every second) line
of symbols which are the encoded value of the field. Format:

`^board=(.*)$`

You can use this regular expression to extract a board from
the resulting string.

## Field example

Here is an example of a string from the server.

<pre>board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼             ☼☼      ☻      ☼☼            ☺☼☼            ▲☼☼            ║☼☼            ║☼☼            ║☼☼            ╙☼☼             ☼☼             ☼☼             ☼☼             ☼☼             ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The line length is equal to the field square. If to insert a wrapping
character (carriage return) every `sqrt(length(string))` characters,
you obtain the readable image of the field.

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

The first line symbol corresponds with the cell located in the bottom 
left corner and has the coordinates `[0, 0]`. In this example, this is 
the position of the snake's head
(symbol `▲`) — `[13, 10]`, and stone (symbol `☻`) - `[7, 12]`.

## Symbol breakdown

Please [check it here](elements.md).

## What to do

The game is a step-by-step process which has the server send its client (bot)
the present state of the updated field, and then wait for the response 
of the command for the snake. Within the next second the player much 
give a command to the snake. If they don't manage to give one in time, 
the snake will move automatically (until it finds a wall).

Your goal is to make the hero move according to your algorithm. The
algorithm must earn points as much as possible. The ultimate goal is
winning the game.

## Commands

There are several commands
* `UP`, `DOWN`, `LEFT`, `RIGHT` - which lead to turning 
  the snake's head in the required direction. I would like to remind you 
  that the snake moves mechanically under its own inertia.

## Points

The parameters will change[*](index-md.md#ask) as the game progresses.

## Cases

## <a id="ask"></a> Ask Sensei

You can always see the settings of the current game
[here](/codenjoy-contest/rest/settings/player).
Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Hints

First, make the snake listen to your commands. If there is no client for 
your language, it becomes slightly more complicated — you will have to 
write a WebSocket for the client. Thus, you prepare for the main game. 
Your next job is to write a bot which will follow a specific strategy on 
the field and will win.

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
* `Board`
  Еncapsulating the line with useful methods for searching
  elements on the board. The following methods can be found in the board:
* `int boardSize();`
  Size of the board
* `boolean isAt(Point point, Element element);`
  Whether the given element has given coordinate?
* `boolean isAt(Point point, Collection<Element>elements);`
  Whether any object from the given set is located in given coordinate?
* `boolean isNear(Point point, Element element);`
  Whether the given element is located near the cell with the given coordinate?
* `int countNear(Point point, Element element);`
  How many elements of the given type exist around the cell with given coordinate?
* `Element getAt(Point point);`
  Element in the current cell.
* etc...

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy.git).
All instructions are in Readme.md file, you'll know what to do next once you read it.

If you have any questions reach me in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).

Good luck and may the best win!