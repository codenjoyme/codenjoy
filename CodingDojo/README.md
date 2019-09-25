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
- install [Lombok plugin](https://plugins.jetbrains.com/plugin/6317-lombok) for idea

Run Codenjoy server from sources
--------------------------------

To run a project with your game, do the following:
- clone the project from the [codenjoy main repository](https://github.com/codenjoyme/codenjoy)
- configure Codenjoy server by modifying the settings in files:
  * [\CodingDojo\server\src\main\resources\application.yml](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/server/src/main/resources/application.yml)
  * [\CodingDojo\server\src\main\webapp\resources\js\init.js](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/server/src/main/webapp/resources/js/init.js)
- run `mvn clean install -DskipTests=true` in the `\CodingDojo\games\engine` project to install the common classes/interfaces
- build one game
  * run `mvn clean install -N -DskipTests=true` in the `\CodingDojo\games`
     project to install only games parent project
  * run `mvn clean install -DskipTests=true` in the `\CodingDojo\games\yourgame`
     project to install your game
  * run `mvn clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,yourgame,debug -Dserver.port=8080 -Pyourgame`
     in the `\CodingDojo\server` project to launch the game (where 'yourgame')
     is a name of profile that you have set recently in `\CodingDojo\server\pom.xml`.
     There may be several games listed separated by commas.
- build all games
  * run `mvn clean install -DskipTests=true` in the `\CodingDojo\games` project to install all games
  * If you want to run all games just run `mvn clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug -Dcontext=/codenjoy-contest --server.port=8080 -DallGames`
- if maven is not installed on you machine, try `mvnw` instead of `mvn`
- a simpler way of launching Codenjoy with all games is by running a script in the root `\CodingDojo\build-server.bat` then `\CodingDojo\start-server.bat`
  * please change `set GAMES_TO_RUN=tetris,snake,bomberman` before run `\CodingDojo\build-server.bat`
  * also you can change properties `--spring.profiles.active=sqlite,debug --context=/codenjoy-contest --server.port=8080` inside `\CodingDojo\start-server.bat`
    * `context` changes link to the application
    [http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest)
    * `server.port` the port on which the application starts
    * `spring.profiles.active`
      * `sqlite` for the lightweight database (<50 participants)
      * `postgres` for the postgres database (>50 participants)
      * `trace` for enable log.debug
      * `debug` if you want to debug js files (otherwise it will compress and obfuscate)
      * `yourgame` if you added your custom configuration to the game inside `CodingDojo\games\yourgame\src\main\resources\application-yourgame.yml`
- after that in the browser access [http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest) and register the player
- you can read a description of any game on the help page [http://127.0.0.1:8080/codenjoy-contest/help](http://127.0.0.1:8080/codenjoy-contest/help)
- in case of any problems, please email [apofig@gmail.com](mailto:apofig@gmail.com) or chat to [Skype Oleksandr Baglai](skype:alexander.baglay)

Run Codenjoy in portable mode
--------------
There are three scripts to run Codenjoy on Ubuntu and Windows:
- [how to run the server on Ubuntu](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker-compose#ubuntu-portable-script)
- [how to run the server on Windows](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/windows-cmd#windows-portable-script)
- [how to run the server on Linux (simple version)](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker#linux-portable-script-simple-version)

Develop a game
--------------
To find out more on how to create a game, [read here](https://github.com/codenjoyme/codenjoy-game#create-your-own-codenjoy-game)

Server Configuration
--------------
## Choosing database
Game server has support of two RDBMS solutions: PostgreSQL and SQLite. Choice of exact solution is implemented by means 
of Spring profile and is mandatory (e.i. server will fail to start if none of database-profiles are chosen).

To choose DB implementation, add one of profiles below into either `--spring.profiles.active` CLI argument or 
`SPRING_PROFILES_ACTIVE` environment variables:

| Spring profile | Purpose                                                                                                                  |
|----------------|--------------------------------------------------------------------------------------------------------------------------|
|  `postgresql`  | Aims game server to PostgreSQL database. <br> **Setting up application-postgresql.yml properties properly is mandatory** |
|    `sqlite`    | Aims game server to SQLite database. <br> **Set up application-sqlite.yml properties to change SQLite files location**   |

## Authorization
Server supports for several authorization and authentication approaches depending on Spring profiles that must be set 
either by `--spring.profiles.active` CLI argument or `SPRING_PROFILES_ACTIVE` environment variable:

|  spring profile |                                                                                                                                                                                                          purpose                                                                                                                                                                                                          |
|:---------------:|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| default profile | If none of authorization specific profiles are set server will run with form-based authorization. User data along with email, password and roles are being stored in `USERS` table in underlying DB<br> See [Choosing database](## Choosing database)                                                                                                                                                                    |
| `oauth2`        | Profile sets up the server to use Authorization Grant Type OAuth2 flow according to provided authorization settings in `application-oauth2.yml`.<br> **Any non-authenticated user is redirected to Authorization Server login form.** (see authorization settings below)                                                                                                                                                      |
| `sso`           | Profile sets up the server to use Authorization Grant Type OAuth2 flow according to provided authorization settings in `application-sso.yml`.<br> **This profile in opposite to `oauth2` one does not support for SA's form-based redirects. It checks `Authorization` http header instead to authorize incoming requests. Server running in this profile is meant to be deployed behind authorization aware reverse-proxy** |    


### OAuth2 & SSO settings
`oauth2` and `sso` profiled server instance must be provided with proper OAuth2 specific settings in application context via CLI arguments or env. vars according to spring framework specification.
  
Those mandatory settings are:

|                                         application property                                         | env. variable <br> (if supported) | Purpose                                                                                                                                                                                                                                                                                                                                                                                                           |
|------------------------------------------------------------------------------------------------------|-----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `auth-server.location`                                                                               | `OAUTH2_AUTH_SERVER_URL`          | Authorization Server URL                                                                                                                                                                                                                                                                                                                                                                                          |
| `spring.security.oauth2.client.registration.dojo.client-id`                                          | `OAUTH2_CLIENT_ID`                | OAuth2 `client_id`. Client with specified ID must be registered on AS                                                                                                                                                                                                                                                                                                                                             |
| `spring.security.oauth2.client.registration.dojo.client-secret`                                      | `OAUTH2_CLIENT_SECRET`            | OAuth2 `client_secret`. Client secret for `client_id`                                                                                                                                                                                                                                                                                                                                                             |
| `spring.security.oauth2.client.registration.dojo.scope`<br> Default value is: `openid profile email` | `OAUTH2_SCOPES`                   | OAuth2 scopes used for Resource Server resources restriction.<br> Few standard scopes:<br> • `profile` - includes user name information into userinfo endpoint response and/or into Identity Tokens<br>  • `email` - includes email address into userinfo endpoint response and/or into Identity token<br>  • `id_token` - *OpenID Connect specific scopes*. Requests AS to enroll Identity Token along with `access_token` |
| `spring.security.oauth2.client.provider.dojo.authorization-uri`                                      | `OAUTH2_AUTH_URI`                 | AS Authorization URI (part after `auth-server.location`)<br> Provides user with AS authentication form.<br> For more details see OAuth2 RFC                                                                                                                                                                                                                                                                       |
| `spring.security.oauth2.client.provider.dojo. token-uri`                                             | `OAUTH2_TOKEN_URI`                | AS Token URI (part after `auth-server.location`)<br> Provides an `access_token` (and `id_token` if AS supports OpenID Connect).<br>  For more details see OAuth2 RFC                                                                                                                                                                                                                                              |
| `spring.security.oauth2.client.provider.dojo.jwk-set-uri`                                            | `OAUTH2_JWKS_URI`                 | AS JWKS URI (part after `auth-server.location`)<br> Provides information about AS signature algorithms and Public Keys. *Only for OpenID Connect compatible AS*<br>  For more details see OIDC RFC                                                                                                                                                                                                                |
| `spring.security.oauth2.client.provider.dojo.user-info-uri`                                          | `OAUTH2_USERINFO_URI`             | AS Userinfo URI (part after `auth-server.location`)<br> Provides information about user depending on requested scopes and in response to properly authorized request<br>  For more details see OAuth2 RFC                                                                                                                                                                                                         |
| `spring.security.oauth2.client.provider.dojo.user-name-attribute`                                    | `OAUTH2_USERNAME_ATTR`            | Key for the user name attribute in AS response to Userinfo endpoint request  For more details see OAuth2 RFC                                                                                                                                                                                                                                                                                                      | 

For oauth2 try run `clean install spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug,oauth2 -Dcontext=/codenjoy-contest -DallGames -DOAUTH2_AUTH_SERVER_URL=https://authorization-server.com/core -DOAUTH2_AUTH_URI=/connect/authorize -DOAUTH2_CLIENT_ID=dojo -DOAUTH2_CLIENT_SECRET=secret -DOAUTH2_TOKEN_URI=/connect/token -DOAUTH2_USERINFO_URI=/connect/userinfo -DCLIENT_NAME=dojo`
Then try go to [/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest) from browser, follow authorize steps and play the game.

For sso try run `clean install spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug,oauth2 -Dcontext=/codenjoy-contest -DallGames -DOAUTH2_AUTH_SERVER_URL=https://authorization-server.com/core -DOAUTH2_AUTH_URI=/connect/authorize -DOAUTH2_CLIENT_ID=dojo -DOAUTH2_CLIENT_SECRET=secret -DOAUTH2_TOKEN_URI=/connect/token -DOAUTH2_USERINFO_URI=/connect/userinfo -DCLIENT_NAME=dojo`
Then try go to [/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest) from browser, follow authorize steps and play the game.
```
<oauth>
<error_description>
Full authentication is required to access this resource
</error_description>
<error>unauthorized</error>
</oauth>
```
Don't worry about it. Just download [postman](https://www.getpostman.com/downloads/) and create `GET` request:
- `http://127.0.0.1/codenjoy-contest/board/rejoining/bomberman`
- `Authorization` -> `Bearer Token` = `USER_JWT_TOKEN_FROM_AUTHORIZATION_SERVER`
After submit you can see html page with board, try find inside:
```
<body style="display:none;">
    <div id="settings" page="board" contextPath="/codenjoy-contest" gameName="bomberman"
        playerName="t8o7ty34t9h43fpgf9b8" readableName="Stiven Pupkin" code="3465239452394852393"
        allPlayersScreen="false"></div>
```
Another way to add `Authroization: Bearer USER_JWT_TOKEN_FROM_AUTHORIZATION_SERVER` header parameter.
Also you can use [https://jwt.io/](https://jwt.io/) to parse `USER_JWT_TOKEN_FROM_AUTHORIZATION_SERVER` and get additional data.

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy#codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========