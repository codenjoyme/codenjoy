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
found [here](../../../resources/snakebattle/user/clients.zip)

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

Player has to [registers on the server](../../../register?gameName=a2048)
using their email address.

Then you should connect [from code](../../../resources/a2048/user/clients.zip)
to the server via Web Sockets. This is the Maven project and it will suit for
playing in JVM languages. To know how to launch it, refer to the root of README.txt

For the other languages, you should write a native client (and then to share it with us by Email: apofig@gmail.com)

Address to connect the game on the http://codenjoy.com server:

`ws://codenjoy.com:80/codenjoy-contest/ws?user=[user]&code=[code]`

Address to connect the game on the server deployed in the local area network (LAN):

`ws://[server]:8080/codenjoy-contest/ws?user=[user]&code=[code]`

Here `[server]` - ip/domain address of server, `[user]` is your
player id and `[code]` is your security token - you can get
it from browser address bar after registration/login.

## Message format

After connection, the client will regularly (every second) receive a line of
characters with the encoded state of the field. The format:

`^board=(.*)$`

With the help of regexp you can obtain a board line. Example of the line from the server:

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

[Interpretation of characters](elements.md)

The game is turn-based, every second the server sends to its
client (bot) the state of the updated field at the moment and
expects a response of the team. Within the next second, the
player should give a command. If the player lost his/her chance,
nothing changes

The first task is to write a client’s WebSocket which will
connect to the server. Then you should “force” digits on the
field to listen to the commands. This is the way the gamer
will prepare herself/himself to the main game. The primary
goal is to play meaningfully and win.