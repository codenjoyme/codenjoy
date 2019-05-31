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

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========