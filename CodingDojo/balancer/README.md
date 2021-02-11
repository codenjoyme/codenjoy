Codenjoy Balancer
==============

Used as a facade for multiple Codenjoy servers.

Make sure all important components already started:
- [How to launch the game server from sources](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo#run-codenjoy-server-from-sources)
- [How to launch the balancer frontend from sources](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/balancer-frontend#available-scripts)

Run Balancer from sources
--------------------------------

To run a project with your game, do the following:
- clone the project from the [codenjoy main repository](https://github.com/codenjoyme/codenjoy)
- configure Balancer server by modifying the settings in files:
  * [\CodingDojo\balancer\src\main\resources\application.yml](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/balancer/src/main/resources/application.yml)
    * you can change any of these properties in runtime by using parameters 
      * `mvn ... -Dkey1=value1 -Dkey2=value2`
      * `java -jar ... --key1=value1 --key2=value2`
    * Important options are
      * `context` changes link to the application [http://127.0.0.1:8081/codenjoy-balancer](http://127.0.0.1:8081/codenjoy-balancer)
      * `spring.profiles.active`
        * `sqlite` for the lightweight database (<50 participants)
        * `postgres` for the postgres database (>50 participants)
          * `database.host` database server host, `localhost` by default
          * `database.port` database server port, `5432` by default
          * `database.name` database name, `codenjoy` by default
          * `database.user` username to connect, `codenjoy` by default
          * `database.password` password to connect, `securePostgresDBPassword` by default
        * `trace` for enable log.debug
        * `debug` if you want to debug js files (otherwise it will compress and obfuscate)
      * `server.port` application port, use 8081 because 8080 is busy by Codenjoy server 
      * `game.type` game that you want to run
      * `game.servers` CSV list of Codenjoy game servers
      * `room` count rooms 
      * `start-day` start day of tournament in `YYYY-MM-DD` format
      * `end-day`  day of tournament in `YYYY-MM-DD` format
      * `finalists-count` number of daily finalists
      * `final-time` final time in `HH-MM` format
- run [Codenjoy server](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo#run-balancer-server-from-sources) in the `\CodingDojo\server` project
- run Balancer server in the `\CodingDojo\balancer` project 
  * run `mvn clean install -DskipTests=true`  
  * run `mvn clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug -Dcontext=/codenjoy-balancer -Dserver.port=8081 -Dgame.type=yourgame -Dgame.servers=localhost:8080 -Droom=1 -Dstart-day=2020-03-01 -Dend-day=2020-03-31 -Dfinalists-count=10 -Dfinal-time=19:00`
- if maven is not installed on you machine, try `mvnw` instead of `mvn`
- a simpler way of launching Balancer is by running a script in the root `\CodingDojo\build-balancer.bat` then `\CodingDojo\start-balancer.bat` (do not forget to start codenjoy server before) 
- another way to run Balancer from war
  * build war file `mvn clean package -DskipTests=true` in the `\CodingDojo\balancer` project to build balancer
  * run war like jar file `java -jar codenjoy-balancer.war --spring.profiles.active=sqlite,debug --context=/codenjoy-balancer --server.port=8081 --game.type=yourgame --game.servers=localhost:8080 --room=1 --start-day=2020-03-01 --end-day=2020-03-31 --finalists-count=10 --final-time=19:00` in the `\CodingDojo\balancer\target`              
- after that in the browser access [http://127.0.0.1:8081/codenjoy-balancer/resources/html/admin.html](http://127.0.0.1:8081/codenjoy-balancer/resources/html/admin.html) 
  * login as Admin 
    * `admin@codenjoyme.com`
    * `admin`
- in case of any problems, please email [apofig@gmail.com](mailto:apofig@gmail.com) or chat to [Skype Oleksandr Baglai](skype:alexander.baglay)

 