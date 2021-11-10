<header class="entry-header">
    <h1 class="entry-title">2048 Codenjoy — how to play? <a href="resources/a2048/help/index.html">[RU]</a> </h1>
</header>

<div class="entry-content">
<div class="page-restrict-output">
<p>The game is based on
<a href="http://gabrielecirulli.github.io/2048/">
http://gabrielecirulli.github.io/2048/</a>. Thank Authors for the idea.</p>

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

<h2>What is the gist of the game?</h2>

<p>You should get to 2048 (or even further). How to do it? With each your
move, the “2” digit will appear in the empty angle cells. By using one
of the commands (LEFT, RIGHT, UP, DOWN), you can approach all digits to
one of the sides. At this, two similar adjacent digits convert into one
what is the sum of these two digits.</p>

<p>Points are calculated as a maximum sum of digits on the board for the whole game.</p>

<p>The player with the largest number of points is considered to
be a winner (prior to the due date).</p>

<p>Then the player <a href="register?gameName=a2048">
registers on the server</a>, by indicating her/his Email.</p>

<p>Then you should connect <a href="resources/a2048/user/clients.zip">from code</a>
to the server via Web Sockets. This is the Maven project and it will suit for
playing in JVM languages. To know how to launch it, refer to the root of README.txt</p>

<p>For the other languages, you should write a native client (and then to share it with us by Email: apofig@gmail.com)</p>

<p>Address to connect to the game on the http://codenjoy.com server:</p>

<pre>ws://codenjoy.com:80/codenjoy-contest/ws?user=3edq63tw0bq4w4iem7nb&code=12345678901234567890</pre>

<p>Address to connect the game on the server deployed in the local area network (LAN):</p>

<pre>ws://server_ip:8080/codenjoy-contest/ws?user=3edq63tw0bq4w4iem7nb&code=12345678901234567890</pre>

<p>Here 'user' is your player id and 'code' is your security token, you can get it from browser address bar after registration/login.</p>

<p>After connection, the client will regularly (every second) receive a line of
characters with the encoded state of the field. The format:</p>

<pre>^board=(.*)$</pre>

<p>With the help of regexp you can obtain a board line. Example of the line from the server:</p>

<pre>board=8A4AA2BB88488848442222222</pre>

<p>The line length is equal to the field square (N*N). If to insert a
wrapping character (carriage return) every N characters, we obtain
the readable image of the field. The [0,0] coordinate corresponds to
the left bottom corner.</p>

<pre>8A4AA
2BB88
48884
84422
22222</pre>
<p><a href="resources/a2048/help/elements.html">Interpretation of characters</a></p>

<p>The game is turn-based, every second the server sends to its
client (bot) the state of the updated field at the moment and
expects a response of the team. Within the next second, the
player should give a command. If the player lost his/her chance,
nothing changes</p>

<p>The first task is to write a client’s WebSocket which will
connect to the server. Then you should “force” digits on the
field to listen to the commands. This is the way the gamer
will prepare herself/himself to the main game. The primary
goal is to play meaningfully and win.</p>

</div>
</div>