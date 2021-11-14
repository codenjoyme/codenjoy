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

## Connect to the server

So, the player [registers on the server](../../../register?gameName=kata)
and joining the game.

Then you should connect from client code to the server via websockets.
This [collection of clients](https://github.com/codenjoyme/codenjoy-clients.git)
for different programming languages will help you. How to start a
client please check at the root of the project in the README.md file.

To realize your algorithm-puzzle, you should
implement the following interface:
`CodingDojo\games\kata\src\main\java\com\codenjoy\dojo\kata\model\levels\Level.javа`
and locate it nearby in the `algorithms` package.

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

You do not need to work with this JSON if you have a realized client.
To know how to launch a client, refer to the README.txt. in the
root of the project.

## What to do

The game is turn-based, every second the server sends to your client
(bot) questions on the currently active tests and expects a response.
Within the next second, the player should give this response. If the
player lost his/her chance, it is not a big deal: nothing will happen
and the server will repeat its questions.

Your goal is to make the hero move according to your algorithm. The
algorithm must earn points as much as possible. The ultimate goal is
winning the game.

## Commands

Responses are given by the message command. Its format is that each
question in input JSON should be answered. All responses are packed
to the array (see below). It is better not to skip questions, the
order of questions should correspond to the order of questions in input
JSON.

`message('['answer1','answer2','answer3']')`

## Points

## Cases

## <a id="ask"></a> Ask Sensei

Please ask Sensei about current game settings. You can find Sensei in
the chat that the organizers have provided to discuss issues.

## Hints

The first task is to write a client’s WebSocket which will connect to the
server. Then you should “force” the server to listen to the client answer.
This is the way the gamer will prepare herself/himself to the main game.
The main goal is to play meaningfully and win.

## Clients and API

The client code does not give a considerable handicap to gamers because
you should spend time to puzzle out the code. However, it is pleasant
to note that the logic of communication with the server plus some high
level API for working with the board are implemented already.

* `Solver`
  An empty class with one method — you'll have to fill it with smart logic.
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