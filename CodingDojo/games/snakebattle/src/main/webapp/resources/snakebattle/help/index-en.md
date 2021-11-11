<header class="entry-header">
<h1 class="entry-title">Snake Battle — как играть? <a target="_self" href="index.html">[RU]</a> </h1>
</header>

<div class="entry-content">
<div class="page-restrict-output">

<p>The game server is available for familiarization reasons
<a href="http://codenjoy.com/codenjoy-contest">
http://codenjoy.com/codenjoy-contest</a></p>

<p>This is the open source game. To realize your game, correct errors in the current
version and make the other corrections, you should
<a href="https://github.com/codenjoyme/codenjoy">fork the project</a> at first.
There is the description in the Readme.md file in the repository root.
It is specified in the description what to do next.</p>

<p> If any questions, please write in <a href="skype:alexander.baglay">skype:alexander.baglay</a>
or Email <a href="mailto:apofig@gmail.com">apofig@gmail.com</a></p>

<p><b>Game project (for writing your bot) can be found <a href="../../../resources/snakebattle/user/clients.zip">here</a></b></p>

<p>Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.</p>

<h2>В чем суть игры?</h2>

<p>You have to write a snake bot that will beat other bots by points. All players play on the
same field. Snake can move to empty cells in four directions but cannot move to the previous cell.</p>

<p>On its path a snake can encounter stones, gold, fury pills, flying pills, aplles or other
snakes. If the snake eats a stone it becomse shorter by 4 cells. If its length is less
than 2, it dies. For gold, aplles and dead competitors the snake gets bonus points.
For its own death and eaten stones the snake gets demerit points.
The points are summed up.</p>

<p>The player with most points when time expires wins. A dead snake immediately
vanishes and reappears on one of the respawn cells, waiting for the next
round (start event).</p>

<p>So, the players <a href="/codenjoy-contest/register?gameName=snakebattle">
register on the server</a>and enter their email addresses.</p>

<p>After that they have to connect to server <a href="../../../resources/snakebattle/user/clients.zip">from code</a>
through websockets. This is a Maven project and it will work for games on JVM languages.
Refer to README.txt in the root to see how to launch it.</p>

<p>For other languages you will have to write your own client and share it with us via email: apofig@gmail.com</p>

<p><a htef="http://codenjoy.com">Codenjoy Server</a> game connection address:</p>

<pre>ws://codenjoy.com:80/codenjoy-contest/ws?user=3edq63tw0bq4w4iem7nb&code=12345678901234567890</pre>

<p>LAN server game connection address:</p>

<pre>ws://server_ip:8080/codenjoy-contest/ws?user=3edq63tw0bq4w4iem7nb&code=12345678901234567890</pre>

<p>here 'user' - player id and 'code' - your security token. You can get if from browser address bar of board
page after registration/login.</p>

<p>After the connection is established, the client will regularly (every second)
receive a symbol string with encoded state of the field.</p>

<p>String length equals field area. Adding hyphen every sqrt(length(string))
symbols will give a comprehensible view of the field..</p>

<p>Field example:</p>

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

<p>Sprite UI:</p>

<img src="./board.jpg"/>

[Interpretation of characters](elements.md)

<p>Snake's body elements can be found in the Elements file.</p>

<p>The game is turn-based: Each second, the server sends the updated state of the
field to the client and waits for response. Within the next second the player
must give the snake a command. If no command is given, the snake moves inertially
in its current direction until stopped by a wall.</p>

<p>There are five commands:<br>
UP, DOWN, LEFT, RIGHT - they move the snake one cell in the
corresponding direction; <br>
ACT - drop a stone (if the snake has previously eaten at
least one). The stone is left at the end of snake's tail. With the help of stones
players can set obstacles and block enemies.</p>

<p>Movement and ACT commands can be combined, separating them by comma. During one game
cycle the snake will drop a stone and move, e.g. (LEFT, ACT) or (ACT, LEFT).</p>

<p>Your goal is to make the snake move according to your algorithm. The algorithm must
block and destroy enemy snakes with the help of bonuses (pills). The ultimate goal
is winning the game.</p>

<p><b>Battle rules</b></p>

<p><b>Negative impact:</b><br>
- Snake that hits a wall, dies.<br>
- Snake that hits another snake, dies.<br>
- Snake must be at least two cells long or it dies.<br>
- Snake that eats a stone becomes three cells shorter, and, if that
makes it shorter than two cells - it dies.</p>

<p><b>Positive impact:</b><br>
- Snake that eats an apple becomes longer by one cell.<br>
- Snake that eats a flying pill flies over stones and other snakes for 10 moves.<br>
- Snake that eats a fury pill can bite off parts of other snakes and eat stones without
negative effects for 10 moves.<br>
- Snake that eats gold gets bonus points.</p>

<p><b>Exceptional cases:</b><br>
- Snakes can bite off their own tails, becoming shorter without any negative effects.<br>
- If two snakes collide head-on, the shortest snake dies. The surviving snake becomes
shorter - by the length of the dead one (if that makes it shorter than two cells, it dies as well).<br>
- The bitten off part of the tail always disappears, and the snake is shortened.
- If two snakes, one of which is under the flying pill, collide, nothing happens.<br>
- If two snakes collide, the under the fury pill always wins.<br>
- If two furious snakes collide, common collision rules are used.</p>

<p>Good luck and let the smartest ass win!</p>

<p><b>Hints:</b><br>
For your algorithm you can use the existing class DeikstraFindWay and getShortestWay() metod in particular.<br>
If you are not sure what to do try to implement the following algorithms:<br>
- Move to a random empty adjacent cell.<br>
- Move to a free cell in the direction of the nearest apple.<br>
- Move to an apple that can be reached faster.<br>
- Avoid longer enemies and ones under fury pill.<br>
- Block the supposed path of the enemy by your tail.<br>

<p>Maximum number of players is defined by the number of respawn points. 15 for the current map.</p>
</div>

<div class="entry-author">

<p>If you have any questions feel free to contact me:<br>
Author: <b>Корсиков Илья</b><br>
mail: <b>kors.ilya@gmail.com</b><br>
skype: <b>kk.ilya</b></p>

<p>Or <a href="skype:alexander.baglay">skype:alexander.baglay</a><br>
email <a href="mailto:apofig@gmail.com">apofig@gmail.com</a></p>

</div>
    
</div>