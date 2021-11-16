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

The game is based on
[http://gabrielecirulli.github.io/2048](http://gabrielecirulli.github.io/2048).
Thank Authors for the idea.

Game project (for writing your bot) can be
found [here](https://github.com/codenjoyme/codenjoy-clients.git)

## What is the game about

Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.

You should get to 2048 (or even further). How to do it? With each your
move, the “2” digit will appear in the empty angle cells. By using one
of the commands (LEFT, RIGHT, UP, DOWN), you can approach all digits to
one of the sides. At this, two similar adjacent digits convert into one
what is the sum of these two digits.

Points are calculated as a maximum sum of digits on the board for the whole game.

The player with the largest number of points is considered to
be a winner (prior to the due date).

## Connect to the server

So, the player [registers on the server](../../../register?gameName=s2048)
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

In the client code, you need to find a similar line and replace it
with your URL - thereby, you set the login / password to access the
server. Then start your client and make sure the server receives
your client's commands. After that, you can start working on the
logic of the bot.

## Message format

After connection, the client will regularly (every second) receive a line of
characters with the encoded state of the field. The format:

`^board=(.*)$`

You can use this regular expression to extract a board from
the resulting string.

## Field example

Here is an example of a string from the server.

<pre>board=8A4AA2BB88488848442222222</pre>

The line length is equal to the field square (N*N). If to insert a
wrapping character (carriage return) every N characters, we obtain
the readable image of the field. The `[0,0]` coordinate corresponds to
the left bottom corner.

<pre>8A4AA
2BB88
48884
84422
22222</pre>

## Symbol breakdown

Please [check it here](elements.md).

## What to do

The game is turn-based: Each second, the server sends the updated state
of the field to the client and waits for response. Within the next
second the player must give the hero a command. If no command is
given, the hero will stand still.

Your goal is to make the hero move according to your algorithm. The
algorithm must earn points as much as possible. The ultimate goal is
winning the game.

## Commands

* `UP`, `DOWN`, `LEFT`, `RIGHT` - “force” digits in the specified
  direction.

## Points

The parameters will change[*](index-md.md#ask) as the game progresses. 

## Cases

## <a id="ask"></a> Ask Sensei

You can always see the settings of the current game
[here](/codenjoy-contest/rest/settings/player).
Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Hints

The first task is to write a client’s WebSocket which will
connect to the server. Then you should “force” digits on the
field to listen to the commands. This is the way the gamer
will prepare herself/himself to the main game. The primary
goal is to play meaningfully and win.

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

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy.git).
All instructions are in Readme.md file, you'll know what to do next once you read it.

If you have any questions reach me in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).

Good luck and may the best win!
