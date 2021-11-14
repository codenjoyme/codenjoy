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

So, the player [registers on the server](../../../register?gameName=snakebattle)
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

After the connection is established, the client will regularly (every second)
receive a symbol string with encoded state of the field.

The format is as follows:

`^board=(.*)$`

You can use this regular expression to extract a board from
the resulting string.

## Field example

Here is an example of a string from the server.

<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼  ○                       ●☼☼#                           ☼☼☼      ☼#         ●         ☼☼☼                           ☼☼#                        æ  ☼☼☼                ☼#      │  ☼☼☼      ☼☼☼        ☼  ☼   │  ☼☼#      ☼          ☼  ☼   │  ☼☼☼      ☼        ● ☼  ☼   │  ☼☼☼      ☼☼☼æ       ○   ● ┌┘  ☼☼#         │    ☼#     <─┘ ● ☼☼☼     ○<──┘  ●           æ  ☼☼☼     ▲             ☼   ˄│  ☼☼#     ║                 └┘  ☼☼☼     ║                     ☼☼☼     ║           ☼#     ●● ☼☼#     ║ ☼☼ ☼                ☼☼☼     ║    ☼                ☼☼☼  ●  ║ ☼☼ ☼                ☼☼#     ║    ☼             ●  ☼☼☼ ● ╘═╝   ☼#                ☼☼☼     ●           ○         ☼☼#                  ☼☼☼   ●  ☼☼☼                        ● ●☼☼☼               ☼☼☼#        ☼☼#                           ☼☼☼                           ☼☼☼                           ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The length of the string is equal to the area of the field.
If you insert a hyphen character strings every
`sqrt(length(string))` characters, then you will get a readable
field image.

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

The first character of the line corresponds to a cell located on the
left-top corner and has the `[0, 28]` coordinate. The following example
shows the position of the hero (the `▲` character) – `[7,16]`. left-bottom
corner has the `[0, 0]` coordinate.

This is what you see on UI

![](board.jpg)

## Symbol breakdown

Please [check it here](elements.md).

## What to do

The game is turn-based: Each second, the server sends the updated state of the
field to the client and waits for response. Within the next second the player
must give the snake a command. If no command is given, the snake moves inertially
in its current direction until stopped by a wall.

## Commands

* `UP`, `DOWN`, `LEFT`, `RIGHT` - they move the snake one cell in the
  corresponding direction.
* `ACT` - drop a stone (if the snake has previously eaten at
  least one). The stone is left at the end of snake's tail. With the help 
  of stones players can set obstacles and block enemies.
* Movement and `ACT` commands can be combined, separating them by comma. 
  During one game cycle the snake will drop a stone and move, 
  e.g. `LEFT,ACT` or `ACT,LEFT`.

## Points

The parameters will change[*](index-md.md#ask) as the game progresses.

## Cases

### Negative impact

- Snake that hits a wall, dies.
- Snake that hits another snake, dies.
- Snake must be at least two cells long or it dies.
- Snake that eats a stone becomes three cells shorter, and, if that
  makes it shorter than two cells - it dies.

### Positive impact

- Snake that eats an apple becomes longer by one cell.
- Snake that eats a flying pill flies over stones and other snakes for 10 moves.
- Snake that eats a fury pill can bite off parts of other snakes and eat stones without
  negative effects for 10 moves.
- Snake that eats gold gets bonus points.

### Exceptional cases

- Snakes can bite off their own tails, becoming shorter without any negative effects.
- If two snakes collide head-on, the shortest snake dies. The surviving snake becomes
  shorter - by the length of the dead one (if that makes it shorter than two cells, it dies as well).
- The bitten off part of the tail always disappears, and the snake is shortened.
- If two snakes, one of which is under the flying pill, collide, nothing happens.
- If two snakes collide, the under the fury pill always wins.
- If two furious snakes collide, common collision rules are used.

## <a id="ask"></a> Ask Sensei

You can always see the settings of the current game
[here](/codenjoy-contest/rest/settings/player).
Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Hints

Your goal is to make the snake move according to your algorithm. The algorithm must
block and destroy enemy snakes with the help of bonuses (pills). The ultimate goal
is winning the game.

For your algorithm you can use the existing class DeikstraFindWay and 
getShortestWay() metod in particular.

If you are not sure what to do try to implement the following algorithms:

- Move to a random empty adjacent cell.
- Move to a free cell in the direction of the nearest apple.
- Move to an apple that can be reached faster.
- Avoid longer enemies and ones under fury pill.
- Block the supposed path of the enemy by your tail.

Maximum number of players is defined by the number of respawn points. 
15 for the current map.

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

## Game author

If you have any questions feel free to contact me:
Author **Корсиков Илья**,
email [kors.ilya@gmail.com](mailto:kors.ilya@gmail.com),
skype [kk.ilya](skype:kk.ilya).

## Want to host an event?

It's an open source game. To implement your version of it,
to fix bugs and to add any other logic simply
[fork it](https://github.com/codenjoyme/codenjoy.git).
All instructions are in Readme.md file, you'll know what to do next once you read it.

If you have any questions reach me in [skype alexander.baglay](skype:alexander.baglay)
or email [apofig@gmail.com](mailto:apofig@gmail.com).

Good luck and may the best win!