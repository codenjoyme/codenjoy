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
found [here](../../../resources/kata/user/clients.zip)

## What is the game about

Keep in mind: when writing your bot you should stick to its movement logic.
The rest of the game is ready for you.

There is multitude of algorithmic problems in the queue. You are expected
to solve them one by one in a contest with the colleagues. For that,
you need to write a native bot which will resolve all these puzzles.
Each player plays in her/his own field. 

The player with the largest number of points is considered to be a winner
(prior to the due date). The points are counted for the each passed
test (invigorating points) and for the solution of the whole puzzle.
The faster the puzzle is solved, the more points are assigned to the
player.

The game is against the clock, you can make a break between algorithms.
The points are calculated depending on the algorithm complexity and the
time spent.

Important! Verifying tests appear one by one as far as correct answers
are given. If the answer is wrong, the new verifying test will not
appear until the player corrects her/his algorithm.

When all the tests on the server are over, the player goes to sleep,
and the timer is switched off. When ready, the player can launch the
next puzzle by the `message('StartNextLevel')` command. The timer will
run and the time starts to be counted. If the player wants to skip the
puzzle, the `message('SkipThisLevel')` command should be applied.
**Attention**: you cannot return the skipped level.

You can read the puzzle description by clicking the Level:N field which
is located under the player’s name above the field. An alert is open
with the description of the puzzle.

Numbering of puzzles starts with 0 and first puzzle is implemented to
check client’s connection to the server. Points are not counted for the
first puzzle solution.

This is the open source game. To realize your algorithm-puzzle, you should
implement the following interface:
`CodingDojo\games\kata\src\main\java\com\codenjoy\dojo\kata\model\levels\Level.javа`
and locate it nearby in the `algorithms` package.
You can [fork the project](https://github.com/codenjoyme/codenjoy)
at first. Then you should proceed to the Readme.md file in the
repository root and read instructions.

## Connect to the server

Player has to [registers on the server](../../../register?gameName=kata)
using their email address

Then you should connect [from code](../../../resources/kata/user/clients.zip)
to the server via Web Sockets. This is the Maven project and it will suit
for playing in JVM languages. The .NET and JavaScript clients are available
as well. To know how to launch a client, refer to README.txt in the root
of the project.

For the other languages, you should write a native client (and then to share
it with us by using Email apofig@gmail.com)

Address to connect the game on the http://codenjoy.com server:

`ws://codenjoy.com:80/codenjoy-contest/ws?user=[user]&code=[code]`

Address to connect the game on the server deployed in the local area network (LAN):

`ws://[server]:8080/codenjoy-contest/ws?user=[user]&code=[code]`

Here `[server]` - ip/domain address of server, `[user]` is your 
player id and `[code]` is your security token - you can get 
it from browser address bar after registration/login.

## Message format

After connection, the client will regularly (every second) receive JSON
with information on the actions performed. The format:

<pre>{
  'description':'Puzzle description.',
  'history':[
    {
      'answer':'answer1',
      'question':'question1',
      'valid':true
    },
    {
      'answer':'answer1',
      'question':'question2',
       'valid':true
    }
  ],
  'level':0,
  'nextQuestion':'question3',
  'questions':[
    'question1',
    'question2',
    'question3'
  ]
}</pre>

You do not need to work with this JSON if you have a JVM/JS/.NET client.
To know how to launch a client, refer to the README.txt. in the
root of the project.

The game is turn-based, every second the server sends to your client
(bot) questions on the currently active tests and expects a response.
Within the next second, the player should give this response. If the
player lost his/her chance, it is not a big deal: nothing will happen
and the server will repeat its questions.

Responses are given by the message command. Its format is that each
question in input JSON should be answered. All responses are packed
to the array (see below). It is better not to skip questions, the
order of questions should correspond to the order of questions in input
JSON.

`message('['answer1','answer2','answer3']')`

The first task is to write a client’s WebSocket which will connect to the
server. Then you should “force” the server to listen to the client answer.
This is the way the gamer will prepare herself/himself to the main game.
The main goal is to play meaningfully and win.