Ubuntu portable script
======================

How to run server on Ubuntu?
----------------------------
I prepared to you this script so you can run it on ubuntu server.
This version uses docker-composer and docker.
If you want to run it on windows, you should read
[how to run the server on Windows](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/windows-cmd)
- make sure that nginx on host machine is disabled.
- *[Optional]* Please use [dedicated server](https://contabo.com/?show=configurator&server_id=270)
    for every 300-500 participants or [VPS](https://contabo.com/?show=configurator&vserver_id=229)
    if number of participants is less then 50.
- copy all files from [this folder](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker-compose)
    to `/srv/codenjoy` server folder as `root` user
- *[Optional]* If docker/docker-compose are not installed run
```
sudo bash setup-docker.sh
docker-compose --version
```
- edit `.env` [file](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/portable/linux-docker-compose/.env)
  * `CONFIG=true` always true
  * `BUILD_SERVER=true` build server sources
  * `BUILD_BALANCER=false` build balancer sources
  * `TIMEZONE=Europe/Kiev` your timezone inside docker containers (for valid time in logs)
  * `GIT_REPO=https://github.com/codenjoyme/codenjoy.git` git repository you want to build
  * `REVISION=master` revision inside repository (to use master, branch name or revision id)
  * `SKIP_TESTS=true` this will be in the http://your-server.com/codenjoy-contest/
  * `CODENJOY_CONTEXT=codenjoy-contest`
  * `GAME=tetris,bomberman,snake` games to build
    * comma separated for several games.
    * if empty - all games
  * `SPRING_PROFILES=postgres,debug` spring profiles, comma separated
    * `sqlite` for the lightweight database (<50 participants)
    * `postgres` for the postgres database (>50 participants)
    * `trace` for enable log.debug
    * `debug` if you want to debug js files (otherwise it will compress and obfuscate)
    * `yourgame` if you added your custom configuration to the game inside `CodingDojo\games\yourgame\src\main\resources\application-yourgame.yml`
  * `SSL=false` true - if you want to use https instead of http
    (please copy certificate here [portable/linux-docker-compose/ssl-cert](https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/portable/linux-docker-compose/ssl-cert))
  * `DOMAIN=false` true - if you want use domain instead of IP
  * `OPEN_PORTS=false` true - if you want debug all applications inside containers and want to enable port mapping (ports settings are bellow)
  * `BASIC_AUTH=false` true - if you want to set basic authorization for all game server to disable site before start contest
  * `BASIC_AUTH_LOGIN=codenjoy` basic authorization login
  * `BASIC_AUTH_PASSWORD=secureBasicAuthPassword` basic authorization password
  * `ADMIN_PASSWORD=secureAdminPassword` this is password for admin page, please keep it secure (`admin@codenjoyme.com` as login)
  * `EMAIL_HASH=secureEmailHashSoul` this is used for build code from from password and email, please keep it secure
  * if you select postgres database - you should use this settings
    * `CODENJOY_POSTGRES_DB=codenjoy`
    * `CODENJOY_POSTGRES_USER=codenjoy`
    * `CODENJOY_POSTGRES_PASSWORD=securePostgresDBPassword`
    * `CODENJOY_POSTGRES_PORT=8004` port for codenjoy, works if `OPEN_PORTS` is true
  * please set `true` if you want to use wordpress at `http://your-server.com/`
    * `WORDPRESS=false`
    * `WORDPRESS_MYSQL_ROOT_PASSWORD=secureWordpressRootDBPassword`
    * `WORDPRESS_MYSQL_DATABASE=wordpress`
    * `WORDPRESS_MYSQL_USER=wordpress`
    * `WORDPRESS_MYSQL_PASSWORD=secureWordpressDBPassword`
    * `WORDPRESS_PORT=8006` port for wordpress application, works if `OPEN_PORTS` is true
    * `WORDPRESS_MYSQL_PORT=8005` port for wordpress database, works if `OPEN_PORTS` is true
  * true, if you want to setup pgadmin for postgres on this server
    * `PGADMIN=false`
    * `PGADMIN_DEFAULT_PASSWORD=securePGAdminPassword`
    * `PGADMIN_DEFAULT_EMAIL=your@email.com`
    * `PGADMIN_PORT=8007` port for pgadmin, works if `OPEN_PORTS` is true
  * domain settings (if `DOMAIN=false` please ignore this option) (uses in nginx setup)
  * `SERVER_IP=79.143.176.243` your IP
  * `SERVER_DOMAIN=your-domain.com` your domain
  * true if you want to run load balancer on this server
    * `BALANCER=false`
    * `BALANCER_PORT=8001` port for balancer backend, works if `OPEN_PORTS` is true
    * `BALANCER_FRONTEND_PORT=8003` port for balancer frontend, works if `OPEN_PORTS` is true
    * `BALANCER_GAME_SERVERS=game1.your.domain.com,game2.your.domain.com,game3.your.domain.com` list of game servers, comma separated, works if `BALANCER` is true
  * `CODENJOY=true` if you want to run codenjoy app on this server (only one app `BALANCER` | `CODENJOY` should be)
  * `CODENJOY_PORT=8002` port for codenjoy application, works if `OPEN_PORTS` is true
- build and start server by command (everytime when you committed new changes)
```
sudo bash rebuild.sh
```
- restart server if need
```
sudo bash up.sh
```
- now you can press Ctrl-F5 (for clean browser cache) and register
[http://127.0.0.1:8080/codenjoy-contest](http://127.0.0.1:8080/codenjoy-contest)


Useful commands
---------------
- how to get logs from docker images?
```
sudo bash log.sh nginx
sudo bash log.sh codenjoy_db
sudo bash log.sh codenjoy_server
```
- where is application logs?
```
./logs/
```
- where is database?
```
./materials/
```
- where is applications?
```
./applications/
```
- where is config (nginx / codenjoy)? Be carefull this folder will update every time you run any command this manual
```
./config/
```
- where is my ssl certificates?
```
./ssl-cert/
./config/nginx/ssl.conf
```
- how to backup/restore postgres database?
```
cd ./materials & sudo bash backup.sh
cd ./materials & sudo bash restore.sh
```
- how to check all user saves?
```
cd ./materials & sudo bash get-user-saves.sh "Stiven Pupkin"
```
- how to get all users?
```
cd ./materials & sudo bash get-users.sh
```
- hot to check current git revision?
```
cd ./applications & sudo bash check-revision.sh
```
- if you run any command please create bash script and share it with us - it can be useful for us.
```
echo "Be LAZY write script"
```

Other materials
--------------
For [more details, click here](https://github.com/codenjoyme/codenjoy)

[Codenjoy team](http://codenjoy.com/portal/?page_id=51)
===========