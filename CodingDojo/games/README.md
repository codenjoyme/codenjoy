Create your own Codenjoy game
==============

Introduction
==============
[Codenjoy](http://codenjoy.com) - CodingDojo framework for developers. Its goal is
to organize fun teambuilding activities and/or train how to code.
Already now [we have a lot of games on board](http://codenjoy.com/codenjoy-contest).
And you can write one that will be your own.

Set up a development environment
--------------
All you need to develop a game is jdk8, maven3, git, and IDE Idea.

- install a git client locally, for example, [tortoise git](https://code.google.com/p/tortoisegit/)
- create an account on [github](http://github.com) or [bitbucket](http://bitbucket.org)
- make a fork (or copy the sample project) from
[the current repository](https://github.com/codenjoyme/codenjoy-game)
- pull the project to your computer
- install [maven3](https://maven.apache.org/download.cgi) (download the archive and unzip it to `c:\java`)
- add the `M2_HOME` environment variable that points to the root of `c:\java\apache-maven-3.x.x`
- add the `;%M2_HOME%\bin` string at the end of the `Path` variable
- install jdk8, if necessary (also to the folder `c:\java`)
- add the `JAVA_HOME` environment variable that points to the root of `c:\java\jdk1.8.x_xx`
- add the `;%JAVA_HOME%\bin` string at the end of the Path variable
- check by running cmd.exe with the `mvn -version` command.
If installation is successful, you will see the command output the version of
aven and java, rather than "command not found"
```
C:\Users\user>mvn -version
Apache Maven 3.x.x
Maven home: C:\java\apache-maven-3.x.x
Java version: 1.8.x_x, vendor: Oracle Corporation
Java home: C:\java\jdk1.8.x_xx\jre
Default locale: xxxxx, platform encoding: xxxxxxx
OS name: "xxxxxxxxxx", version: "xxx", arch: "xxxxx", family: "xxxxxxx"
C:\Users\user>
```
- download and install [IntelliJ IDEA Community version](https://www.jetbrains.com/idea/download/)
- install [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok) for idea

Install the codenjoy engine
--------------

You have to install the `engine` dependency located in the `engine` folder.
Watch out: its version may be updated, so you will have
to update it and your game's source code. To do this:

- open the `CodingDojo/games/engine` folder
- run `setup.bat`
- make sure installation is successful - the dependency should be
installed under `C:\Users\<UserName>\.m2\repository\com\codenjoy\engine`

Develop a game
--------------

Invent/recall a game that might interest others, does not involve following complicated rules,
and people played it in childhood.

The game might have the same rules as the original,
or might provide some variation. For example, you can turn a two-player game, such as the battleship,
into a multiplayer game. Make sure the gamer will enjoy writing the AI algorithm
(the challenges should be reasonably tough).

Do not hesitate to [contact us](http://codenjoy.com/portal/?page_id=51)
If stumped, get in touch and we'll help you.

Then proceed to writing a model of the game you selected. The following section contains the required how-to.

Here is an example (in russian) of how a 'reversi'
model was written: [part1](https://www.youtube.com/watch?v=W17BKHU9H-Y),
[part2](https://www.youtube.com/watch?v=zrINVp1RFj4),
[par3](https://www.youtube.com/watch?v=zarRXhfqlfM).
You can investigate commits with this command
`git log --oneline 71090be..8a12c53`

Unit testing coverage is expected. It'd be still better to write the code
following TDD, and if you don't know how
[watch this video (in russian)](https://vimeo.com/54862036).

And then [email us](mailto:apofig@gmail.com). We'll arrange for code review if necessary.
And once the model is ready, we'll integrate it to our framework, and
help your arrange your first Codenjoy event.

Here is the repository [https://github.com/codenjoyme/codenjoy-game](https://github.com/codenjoyme/codenjoy-game).
You should find out how to fork a project, how to commit in git.

Sample
--------------

Project [Sample](https://github.com/codenjoyme/codenjoy-game/tree/master/sample) is a sample of a one-board game with all requisite artefacts. Learn how a project operates.

- import the `sample` project as `maven project` to Idea
- run all tests; as they progress, you should observe the green bar
- get [sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java)
and see how tests for games are written.
- get [sample/src/main/java/com/codenjoy/dojo/sample](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample)
and see what are the minimum code requirements for a new game.
- the package here [sample/src/main/java/com/codenjoy/dojo/sample/client](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client)
has client code, with a part of it to be sent to the player as a game template
- the package [sample/src/main/java/com/codenjoy/dojo/sample/client/ai](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai)
has your AI algorithm that will automatically connect to the player and play with him or her.
- all the rest is the game engine.
- before you can implement your new game, take some time to familiarize
yourself with the main interfaces and framework classes. All of them are
located in the dependency engine. You should be interested in these:
  * [engine/src/main/java/com/codenjoy/dojo/services/Game.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Game.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Joystick.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Joystick.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Printer.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/printer/Printer.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/GameType.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/GameType.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/multiplayer/GameField.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/multiplayer/GameField.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/multiplayer/GamePlayer.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/multiplayer/GamePlayer.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Tickable.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Tickable.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/PlayerScores.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/PlayerScores.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Point.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Point.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/GamePrinter.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/GamePrinter.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/EventListener.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/EventListener.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Dice.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Dice.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/State.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/State.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/Direction.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/Direction.java)
  * [engine/src/main/java/com/codenjoy/dojo/services/settings/Settings.java](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/services/settings/Settings.java)
  * [engine/src/main/java/com/codenjoy/dojo/client/AbstractBoard.java](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/client/AbstractBoard.java)
  * [engine/src/main/java/com/codenjoy/dojo/client/Solver.java](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/client/Solver.java)
  * [engine/src/main/java/com/codenjoy/dojo/client/ClientBoard.java](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/games/engine/src/main/java/com/codenjoy/dojo/client/ClientBoard.java)
- all of these are basic interfaces/classes that you use to integrate
the new game into the main framework (like inserting a cartridge into a Dendy console).
- explore their description in java docs for the interfaces and classes
of the dependency engine and the project sample.

Develop a new game
--------------

To develop your game, you don't have to write all classes from
scratch - just base it off the `sample` project.

- copy the contents of the `sample` folder into the `mygame` folder
(any name) and replace the `Sample` word to `MyGame` word in all classes.
- your goal is to have your code well covered with tests, so that we
can trust it. That's why you should develop the game using TDD.
If this is an unfamiliar concept,
[we recommend this book by Ken Beck](http://www.ozon.ru/context/detail/id/1501671/).
- maven will automatically assemble the game source for the client
in a zip file, as shown here
[sample-servers.zip](http://codenjoy.com/codenjoy-contest/resources/user/sample-servers.zip)
Note that the `pom.xml` file has a `maven-antrun-plugin` section,
[where ant assembles](https://github.com/codenjoyme/codenjoy-game/blob/master/pom.xml#L223) this zip. It includes the pom.xml file, the `Elements`
class from the model package, and everything from the `client` package
except the `ai` package.
- write a manual for the game, see an example here:
  * for Russian [sample/src/main/webapp/resources/help/sample.html](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/help/sample.html)
  * for English [sample/src/main/webapp/resources/help/sample-en.html](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/help/sample-en.html)
- draw sprites - square-like pictures that will serve as the basis for
rendering the game in the browser. Normally, they are freely available on the net.
Png files with sprites can be found in the folder [sample/src/main/webapp/resources/sprite/sample/](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/sprite/sample).
Important! Sprite names are not random, they should be associated with enum fields
[sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java).
All names should be lowercase
- then implement your bot by analogy with
[sample/src/main/java/com/codenjoy/dojo/sample/client/ai/AISolver.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai/AISolver.java)
- run DryRunGame class to see how your game works. You can check your bot also.
```
public class DryRunGame {
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new KeyboardSolver(),
                // new AISolver(new RandomDice()),
                new Board());
    }
}
```
- another way to check how it's works is run [sample/src/test/java/com/codenjoy/dojo/sample/SmokeTest.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/test/java/com/codenjoy/dojo/sample/SmokeTest.java)
- assemble a jar file by running the `mvn package` command in the sample folder root
- the jar file will be `sample\target\sample-engine.jar`
- email it to us at [apofig@gmail.com](mailto:apofig@gmail.com) with the `New game for codenjoy` subject
- than we will make a code review and you can improve the game
- Thanks!

Merge your game into codenjoy master
------------------------------------
- renname `sample` folder to `your-game`
- remove everything except `your-game` folder
- commit this changes
- push it to GitHub
- then go to another folder (outside game proect)
- `git clone https://github.com/codenjoyme/codenjoy.git`
- `cd codenjoy`
- `git subtree add --prefix=CodingDojo/games2 https://github.com/yourgithub/your-game.git master`
- renname `CodingDojo/games2` to `CodingDojo/games` with accept merging of two folders
- `git add CodingDojo/games2`
- `git add CodingDojo/games/your-game`
- in the `CodingDojo/server/pom.xml` file, add new profile according to
the template specified in the comment. Profile name should represent game
name for simplicity:
```xml
    <!-- this is your new game
        <profile>
            <id>yourgame</id>
            <activation>
                <property>
                    <name>allGames</name>
                </property>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>${project.groupId}</groupId>
                    <artifactId>yourgame-engine</artifactId>
                    <version>${project.version}</version>
                </dependency>
            </dependencies>
        </profile>
    -->
```
- you must add a new game to the `CodingDojo/games/pom.xml` parent project in the modules section
```xml
    <modules>
        <module>games/engine</module>
        <module>games/sample</module>
        ...
        <module>games/yourgame</module> <!-- this is your new game -->
    </modules>
```
- `git add .`
- `git commit -m "[your-game] Added new game YourGame"
- `git push --all origin`
- then you can [run codenjoy with this game](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo#run-codenjoy-server-from-sources)
- if everything is OK, please prepare Pull Request with your game

Build options
==============
There are several ways how you can build a game in jar. 
Each of them is used for its specific purpose.

Create java-client executable jar
--------------
To package an existing java client into an executable 
jar, run the following command:
```
mvn clean compile assembly:single -DskipTests=true -DgitDir=. -Pjar-with-dependencies
```
Here the `gitDir` parameter is used to specify the 
location of the `.git` directory. You can also use 
`-Pjar-with-dependencies,noGit` to skip git-info phase.

After assembly, a file `<GAMENAME>-engine-exec.jar` 
will appear in `target` so you can run it:
```
java -jar ./target/<GAMENAME>-engine-exec.jar "<CONNECTION_URL>"
```
Here the `<GAMENAME>` is game name that you try to build.
Parameter `<CONNECTION_URL>` is optional - you can override connection URL hardcoded inside YourSolver class 
```
java -jar ./target/<GAMENAME>-engine-exec.jar "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789"
```

Run game engine in a simple mode
--------------
You can run the game without a codenjoy server so that 
it will fully communicate with the ws client, as if the 
server were up. This is useful during game development.

To do this, the game must be able to implement it. 
For example Bomberman game contains a 
[startup class Main](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/games/bomberman/src/main/java/com/codenjoy/dojo/Main.java)
in which the game starts. If your game has the same 
file, you can run the command:
```
mvn clean compile assembly:single -DskipTests=true -DgitDir=. -Pjar-local
```
Here the `gitDir` parameter is used to specify the 
location of the `.git` directory. You can also use 
`-Pjar-local,noGit` to skip git-info phase.

After assembly, a file `<GAMENAME>-engine.jar` will 
appear in `target` so you can run it:
- for windows
```
java -jar -Dhost=127.0.0.1 -Dport=8080 -Dtimeout=1000 -DlogDisable=false -Dlog="output.txt" -DlogTime=true -DshowPlayers="2,3" -Drandom="random-soul-string" -DwaitFor=2 -Dsettings="{'boardSize':11, 'bombPower':7} <GAMENAME>-engine.jar"
```
- for linux
```
java -jar --host=127.0.0.1 --port=8080 --timeout=1000 --logDisable=false --log="output.txt" --logTime=true --showPlayers="2,3" --random="random-soul-string" --waitFor=2 --settings="{'boardSize':11, 'bombPower':7} <GAMENAME>-engine.jar"
```
Here: 
- `<GAMENAME>` is game name that you try to build.
- `host` is always `127.0.0.1`
- `port` any port you want
- `timeout` milliseconds between ticks
- `logDisable` disable log output 
- `log` log file
- `logTime` true if you want to print timestamp 
of each message in log
- `showPlayers` true if you want to print player 
id of each message in log 
- `random` pseudo random soul string - it will affect 
pseudo random. For same string soul pseudo random will 
work the same.
- `waitFor` list of players that we are waiting for 
for before the start
- `settings` json of game settings

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy#codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========