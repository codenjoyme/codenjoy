Windows portable script
=======================

How to run server on Windows?
-----------------------------
Other options:
- If you want to run it on linux, you should read
[how to run the server on Ubuntu](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker-compose#ubuntu-portable-script)
- If you want to run simple version of linex server, you should read 
[how to run the server on Linux (simple version)](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker#linux-portable-script-simple-version)

I prepared to you portable version (for windows) so you can run it without
jdk/maven installation. This version uses their own JDK8, Git and Maven,
so it reset M2_HOME and JAVA_HOME for this session. Nothing will change no
your home version - this works just during run windows command script.
- *[Optional]* Use [this zip](https://epa.ms/EBW39) (~900Mb) - there is everything you need to run on a clean
windows machine without the Internet.
- Copy this folder to root of your HDD. Make sure that there no spaces on the path.
Best use `c:\codenjoy` folder. There is problem with spaces on windows and java
- *[Optional]* Edit `00-setup.bat` for change games to run and other settings,
like git repo, branch name where the server will be built from
  * `set CODENJOY_VERSION=1.1.0` Current version of codenjoy
  * `set SKIP_TESTS=true` should skip test
  * `set GAMES_TO_RUN=ask`
     * if `ask` - we will ask you about this parameter during build
     * if `yourgame` - we will build server with this game only
     * if `game1,game2,game3` - we will build server with all these games
     * if `` - we will build server with all games which we have
  * `set CONTEXT=/codenjoy-contest` changes link to the
    application [http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest)
  * `set PORT=8080` the port on which the application starts
  * `set SPRING_PROFILES=sqlite`
    * `sqlite` for the lightweight database (<50 participants)
    * `postgres` for the postgres database (>50 participants)
    * `trace` for enable log.debug
    * `debug` if you want to debug js files (otherwise it will compress and obfuscate)
    * `yourgame` if you added your custom configuration to the game inside `CodingDojo\games\yourgame\src\main\resources\application-yourgame.yml`
  * `set DEBUG=false` if you want to stop running script after each maven build (successful or no)
  * `set GIT_REPO=https://github.com/codenjoyme/codenjoy.git` repo with codenjoy forked from `https://github.com/codenjoyme/codenjoy.git`
  * `set GIT_REVISION=master` commit where the server will be built from
    * it can be `master`
    * it can be `local` - then we dont touch your local changes
    * it can be a branch name like `snake_with_something`
    * it can be commit hash like `43bd382`
    * or tag name like `v1.1.24`
  * `set REBUILD_SOURCES=ask`
    * if `ask` - we will ask you about this parameter during build,
    * if `yes` - we will rebuild sources
    * if `no` - we'll just run the server with the games that you chose
- *[Optional]* Run `1-rebuild-server.bat` if you just made a changes on
sources `\dojo-server\codenjoy`, want to update from git or change game(s) list
- There are several steps where you should check something and press the
button (it stops only if `00-setup.bat` has `set DEBUG=true`, by default false).
There should always be
```
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.606 s
[INFO] Finished at: 2019-07-12T17:41:34+03:00
[INFO] ------------------------------------------------------------------------
```
not:
```
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD FAILURE
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time:  2.215 s
    [INFO] Finished at: 2019-07-12T17:06:10+03:00
    [INFO] ------------------------------------------------------------------------
```
- Run `2-start-server.bat` for run server. It will immediately open the
page in the browser and 404 will be displayed there. Do not worry, give
the server a start.
```
2019-07-12T18:24:37.779 [main] INFO  o.e.jetty.server.AbstractConnector - Started ServerConnector@435a718a{HTTP/1.1,[http/1.1]}{0.0.0.0:8080}
2019-07-12T18:24:37.782 [main] INFO  o.s.b.w.e.jetty.JettyWebServer - Jetty started on port(s) 8080 (http/1.1) with context path '/codenjoy-contest'
2019-07-12T18:24:37.785 [main] INFO  c.c.dojo.CodenjoyContestApplication - Started CodenjoyContestApplication in 39.97 seconds (JVM running for 86.503)
```
- After that you can press Ctrl-F5 (for clean browser cache) and register
[http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest)

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy#codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========