Create your own Codenjoy game
==============

Introduction
--------------
[Codenjoy](http://codenjoy.com) - CodingDojo framework for developers. Its goal is to organize fun teambuilding activities and/or train how to code.
Already now [you have some games on board](http://codenjoy.com/codenjoy-contest). 
And you can write one that will be your own.

Set up a development environment
--------------
All you need to develop a game is jdk7, maven3, git client, and IDE Idea.

- install a git client locally, for example, [tortoise git](https://code.google.com/p/tortoisegit/)
- create an account on [github](http://github.com) or [bitbucket](http://bitbucket.org)
- make a fork (or copy the sample project) from [the current repository](https://github.com/codenjoyme/codenjoy-game)
- pull the project to your computer
- install [maven3](https://maven.apache.org/download.cgi) (download the archive and unzip it to `c:\java`)
- add the `M2_HOME` environment variable that points to the root of `c:\java\apache-maven-3.x.x`
- add the `;%M2_HOME%\bin` string at the end of the `Path` variable
- install jdk7, if necessary (also to the folder `c:\java`)
- add the `JAVA_HOME` environment variable that points to the root of `c:\java\jdk1.7.x_xx`
- add the `;%JAVA_HOME%\bin` string at the end of the Path variable
- check by running cmd.exe with the `mvn -version` command.
If installation is successful, you will see the command output the version of maven and java, rather than "command not found"
```
C:\Users\user>mvn -version
Apache Maven 3.x.x
Maven home: C:\java\apache-maven-3.x.x
Java version: 1.7.x_x, vendor: Oracle Corporation
Java home: C:\java\jdk1.7.x_xx\jre
Default locale: xxxxx, platform encoding: xxxxxxx
OS name: "xxxxxxxxxx", version: "xxx", arch: "xxxxx", family: "xxxxxxx"
C:\Users\user>
```
- download and install [IntelliJ IDEA Community version](https://www.jetbrains.com/idea/download/)

Install the codenjoy engine
--------------

You have to install the `engine` dependency located in the `engine` folder. Watch out: its version may be updated, so you will have to update it and your game's source code. To do this:

- open the 'engine' folder
- run 'setup.bat'
- make sure installation is successful - the dependency should be installed under `C:\Users\<Твой_юзер>\.m2\repository\com\codenjoy\engine`

Develop a game
--------------

Invent/recall a game that might interest others, does not involve following complicated rules, and people played it in childhood. The game might have the same rules as the original, or might provide some variation. For example, you can turn a two-player game, such as the battleship, into a multiplayer game. Make sure the gamer will enjoy writing the AI algorithm (the challenges should be reasonably tough).
Do not hesitate to [contact us](http://codenjoy.com/portal/?page_id=51)
If stumped, get in touch and we'll help you.

Then proceed to writing a model of the game you selected. The following section contains the required how-to.
[Here is an example](http://apofig.blogspot.com/2011/10/9-tdd.html) of how a snake model was written. Unit testing coverage is  expected. It'd be still better to write the code following TDD, and if you don't know how [watch this video](https://vimeo.com/54862036)
and then write us. We'll arrange for code review if necessary. And once the model is ready, we'll integrate it to our framework, and help your arrange your first codenjoy event.

Here is the repository [https://github.com/codenjoyme/codenjoy-game](https://github.com/codenjoyme/codenjoy-game).
You should find out how to fork a project, how to commit in git.

Sample
--------------

Sample is a sample of a one-board game with all requisite artefacts. Learn how a project operates.

- import the `sample` project as `maven project` to idea
- run all tests; as they progress, you should observe the green bar
- access [sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/test/java/com/codenjoy/dojo/sample/model/SampleTest.java)
and see how tests for games are written.
- access [sample/src/main/java/com/codenjoy/dojo/sample](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample)
and see what are the minimum code requirements for a new game.
- the package here [sample/src/main/java/com/codenjoy/dojo/sample/client](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client)
has client code, with a part of it to be sent to the player as a game template
- the package [sample/src/main/java/com/codenjoy/dojo/sample/client/ai](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai)
has your AI algorithm that will automatically connect to the player and play with him or her.
- all the rest is the game engine.
- before you can implement your new game, take some time to familiarize yourself with the main interfaces and framework classes. All of them are located in the dependency engine. You should be interested in these:
* `src/main/java/com/codenjoy/dojo/services/Game.java`
* `src/main/java/com/codenjoy/dojo/services/Joystick.java`
* `src/main/java/com/codenjoy/dojo/services/Printer.java`
* `src/main/java/com/codenjoy/dojo/services/GameType.java`
* `src/main/java/com/codenjoy/dojo/services/Tick.java`
* `src/main/java/com/codenjoy/dojo/services/PlayerScores.java`
* `src/main/java/com/codenjoy/dojo/services/Point.java`
* `src/main/java/com/codenjoy/dojo/services/Printer.java`
* `src/main/java/com/codenjoy/dojo/services/GamePrinter.java`
* `src/main/java/com/codenjoy/dojo/services/EventListener.java`
* `src/main/java/com/codenjoy/dojo/services/Dice.java`
* `src/main/java/com/codenjoy/dojo/services/CharElements.java`
* `src/main/java/com/codenjoy/dojo/services/Direction.java`
* `src/main/java/com/codenjoy/dojo/services/Settings.java`
* `src/main/java/com/codenjoy/dojo/client/AbstractBoard.java`
* `src/main/java/com/codenjoy/dojo/client/Solver.java`
* `src/main/java/com/codenjoy/dojo/client/Direction.java`
- all of these are basic interfaces/classes that you use to integrate the new game into the main framework (like inserting a cartridge into a Dendy console).
- explore their description in java docs for the interfaces and classes of the dependency engine and the project sample.

Develop a new game
--------------

To develop your game, you don't have to write all classes from scratch - just base it off the `sample` project.

- copy the contents of the `sample` folder into the `mygame` folder (any name) and rename the `Sample` classes to `MyGame` classes.
- your goal is to have your code well covered with tests, so that we can trust it. That's why you should develop the game using TDD. If this is an unfamiliar concept, [we recommend this book by Ken Beck](http://www.ozon.ru/context/detail/id/1501671/).
- maven will automatically assemble the game source for the client in a zip file, as shown here
[sample/src/main/webapp/resources/user/sample-servers.zip](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/user)
Note that the `pom.xml` file has a `maven-antrun-plugin` section, where ant assembles this zip. It includes the pom.xml file, the `Elements` class from the model package, and everything from the `client` package except the `ai` package.
- write a manual for the game, see an example here
[sample/src/main/webapp/resources/help/sample.html](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/help/sample.html)
- draw sprites - square-like pictures that will serve as the basis for rendering the game in the browser. Normally, they are freely available on the net.
Png files with sprites can be found in the folder [sample/src/main/webapp/resources/sprite/sample/](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/webapp/resources/sprite/sample).
Important! Sprite names are not random, they should be associated with enum fields
[sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/model/Elements.java).
All names should be lowercase
- then implement your bot by analogy with [sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java](https://github.com/codenjoyme/codenjoy-game/blob/master/sample/src/main/java/com/codenjoy/dojo/sample/client/ai/ApofigSolver.java)
- run this code to see how your bot plays the game
```
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new YourSolver(new RandomDice()),
                new Board());
    }
```
- assemble a jar file by running the `mvn package` command in the sample folder root
- the jar file will be `sample\target\sample-engine.jar`
- email it to us at [apofig@gmail.com](mailto:apofig@gmail.com) with the `New game for codenjoy` subject

Thanks!

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========