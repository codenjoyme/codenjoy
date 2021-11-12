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
found [here](../../../resources/snakebattle/user/clients.zip)

## What is the game about

Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.

You have to write a snake bot that will beat other bots by points. All players play on the
same field. Snake can move to empty cells in four directions but cannot move to the previous cell.

On its path a snake can encounter stones, gold, fury pills, flying pills, aplles or other
snakes. If the snake eats a stone it becomse shorter by 4 cells. If its length is less
than 2, it dies. For gold, aplles and dead competitors the snake gets bonus points.
For its own death and eaten stones the snake gets demerit points.
The points are summed up.

The player with most points when time expires wins. A dead snake immediately
vanishes and reappears on one of the respawn cells, waiting for the next
round (start event).

## Connect to the server

Player has to [register on the server](../../../register?gameName=snakebattle)
using their email address.

After that they have to connect to server [from code](../../../resources/snakebattle/user/clients.zip)
through websockets. This is a Maven project and it will work for games on JVM languages.
Refer to README.txt in the root to see how to launch it.

For other languages you will have to write your own client and share it with us via email: apofig@gmail.com

Address to connect the game on the http://codenjoy.com server:

`ws://codenjoy.com:80/codenjoy-contest/ws?user=[user]&code=[code]`

Address to connect the game on the server deployed in the local area network (LAN):

`ws://[server]:8080/codenjoy-contest/ws?user=[user]&code=[code]`

Here `[server]` - ip/domain address of server, `[user]` is your
player id and `[code]` is your security token - you can get
it from browser address bar after registration/login.

## Message format

After the connection is established, the client will regularly (every second)
receive a symbol string with encoded state of the field.

String length equals field area. Adding hyphen every `sqrt(length(string))`
symbols will give a comprehensible view of the field..

## Field example

<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼☼  ○                       ●☼
☼#                           ☼
☼☼      ☼#         ●         ☼
☼☼                           ☼
☼#                        æ  ☼
☼☼                ☼#      │  ☼
☼☼      ☼☼☼        ☼  ☼   │  ☼
☼#      ☼          ☼  ☼   │  ☼
☼☼      ☼        ● ☼  ☼   │  ☼
☼☼      ☼☼☼æ       ○   ● ┌┘  ☼
☼#         │    ☼#     <─┘ ● ☼
☼☼     ○<──┘  ●           æ  ☼
☼☼     ▲             ☼   ˄│  ☼
☼#     ║                 └┘  ☼
☼☼     ║                     ☼
☼☼     ║           ☼#     ●● ☼
☼#     ║ ☼☼ ☼                ☼
☼☼     ║    ☼                ☼
☼☼  ●  ║ ☼☼ ☼                ☼
☼#     ║    ☼             ●  ☼
☼☼ ● ╘═╝   ☼#                ☼
☼☼     ●           ○         ☼
☼#                  ☼☼☼   ●  ☼
☼☼                        ● ●☼
☼☼               ☼☼☼#        ☼
☼#                           ☼
☼☼                           ☼
☼☼                           ☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

Sprite UI:

![Board](./board.jpg)

[Interpretation of characters](elements.md)

Snake's body elements can be found in the Elements file.

The game is turn-based: Each second, the server sends the updated state of the
field to the client and waits for response. Within the next second the player
must give the snake a command. If no command is given, the snake moves inertially
in its current direction until stopped by a wall.

There are five commands:
* UP, DOWN, LEFT, RIGHT - they move the snake one cell in the
  corresponding direction;
* ACT - drop a stone (if the snake has previously eaten at
  least one). The stone is left at the end of snake's tail. With the help of stones
  players can set obstacles and block enemies.

Movement and ACT commands can be combined, separating them by comma. During one game
cycle the snake will drop a stone and move, e.g. (LEFT, ACT) or (ACT, LEFT).

Your goal is to make the snake move according to your algorithm. The algorithm must
block and destroy enemy snakes with the help of bonuses (pills). The ultimate goal
is winning the game.

## Battle rules

### Negative impact:
- Snake that hits a wall, dies.
- Snake that hits another snake, dies.
- Snake must be at least two cells long or it dies.
- Snake that eats a stone becomes three cells shorter, and, if that
  makes it shorter than two cells - it dies.

### Positive impact:
- Snake that eats an apple becomes longer by one cell.
- Snake that eats a flying pill flies over stones and other snakes for 10 moves.
- Snake that eats a fury pill can bite off parts of other snakes and eat stones without
  negative effects for 10 moves.
- Snake that eats gold gets bonus points.

### Exceptional cases:
- Snakes can bite off their own tails, becoming shorter without any negative effects.
- If two snakes collide head-on, the shortest snake dies. The surviving snake becomes
  shorter - by the length of the dead one (if that makes it shorter than two cells, it dies as well).
- The bitten off part of the tail always disappears, and the snake is shortened.
- If two snakes, one of which is under the flying pill, collide, nothing happens.
- If two snakes collide, the under the fury pill always wins.
- If two furious snakes collide, common collision rules are used.

Good luck and let the smartest ass win!

## Hints:
For your algorithm you can use the existing class DeikstraFindWay and getShortestWay() metod in particular.

If you are not sure what to do try to implement the following algorithms:

- Move to a random empty adjacent cell.
- Move to a free cell in the direction of the nearest apple.
- Move to an apple that can be reached faster.
- Avoid longer enemies and ones under fury pill.
- Block the supposed path of the enemy by your tail.

Maximum number of players is defined by the number of respawn points. 15 for the current map.

If you have any questions feel free to contact me:
Author **Корсиков Илья**,
email [kors.ilya@gmail.com](mailto:kors.ilya@gmail.com),
skype [kk.ilya](skype:kk.ilya).

Or skype [alexander.baglay](skype:alexander.baglay) or
email [apofig@gmail.com](mailto:apofig@gmail.com).