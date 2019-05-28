Codenjoy
==============
[![Build Status](https://travis-ci.org/codenjoyme/codenjoy.svg?branch=master)](https://travis-ci.org/codenjoyme/codenjoy)

Introduction
--------------
[Codenjoy](http://codenjoy.com) is a framework for developers. Its goal is to organize fun teambuilding activities and/or train how to code.
Already now [you have some games on board](http://codenjoy.com/codenjoy-contest). 
And you can write one that will be your own.

Set up a development environment
--------------
All you need to develop a game is jdk8, maven3, git client and IDE Idea.

- install a git client locally, for example, [tortoise git](https://code.google.com/p/tortoisegit/)
- create an account on [github](http://github.com) or [bitbucket](http://bitbucket.org)
- make a fork (or copy the sample project) from [the current repository](https://github.com/codenjoyme/codenjoy-game)
- pull the project to your computer
- install [maven3](https://maven.apache.org/download.cgi) (download the archive and unzip it to `c:\java`)
- add the `M2_HOME` environment variable that points to the root of `c:\java\apache-maven-3.x.x`
- add the `;%M2_HOME%\bin` string at the end of the `Path` variable
- install jdk7, if necessary (also to the folder `c:\java`)
- add the `JAVA_HOME`environment variable that points to the root of `c:\java\jdk1.8.x_xx`
- add the `;%JAVA_HOME%\bin` string at the end of the Path variable
- check by running cmd.exe with the `mvn -version` command.
If installation is successful, you will see the command output the version of maven and java, rather than "command not found"
```bash
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

Run your game using Codenjoy-contest module
--------------

To build a project with your game, do the following:

- download the project from the [codenjoy main repository](https://github.com/codenjoyme/codenjoy)
- In the `\CodingDojo\server\pom.xml` file, specify the games you need. To achieve this:
- add a dependency to a selected game in the `dependency` block under a new profile. Profile name should 
represent game name for simplicity. 
```xml
<profile>
    <id>sampleengine</id>
    <activation>
        <property>
            <name>allGames</name>
        </property>
    </activation>
    <properties>
        <exclude.sampletext>false</exclude.sampletext>
    </properties>
    <dependencies>
        <dependency>
            <groupId>${project.groupId}</groupId>
            <artifactId>sample-engine</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</profile>
```
- you can add a new game to the `\CodingDojo\pom.xml` parent project in the modules section, or maintain it separately
```xml
<modules>
    <module>games/engine</module>
    <module>games/sample</module>
    ...
    <module>games/your-game</module> <!-- this is your new game -->
    <module>server</module>
</modules>
```
- configure codenjoy by modifying the settings in the file `\CodingDojo\server\src\main\resources\application.yml`
- configure codenjoy by modifying the settings in the file `\CodingDojo\server\src\main\webapp\resources\js\init.js`
- that is, set `email.verification=false` to disable email verification during registration
- run `mvn clean install` in the `\CodingDojo\games\engine` project to install the UI
- run `mvn clean install` in the project root to install all other components
- run `mvn -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring-boot.run.profiles=sqlite spring-boot:run -Pyour-game-profile` 
in the `\CodingDojo\server` project to launch the game (where 'your-game-profile') is a name of profile that you have set 
recently in `\CodingDojo\server\pom.xml`
- a simpler way of launching the game is by running a script in the root of the `\CodingDojo\server\start-server.bat` project
- in the browser, access [http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest) and register the player
- you can read a description of any game on the help page [http://127.0.0.1:8080/codenjoy-contest/help](http://127.0.0.1:8080/codenjoy-contest/help)
- in case of any problems, skype Oleksandr Baglai at `alexander.baglay`

Develop a game
--------------
To find out more on how to create a game, [read here](https://github.com/codenjoyme/codenjoy-game)

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========