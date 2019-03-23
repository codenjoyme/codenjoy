- copy this sctipt [https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/portable/linux-docker/start.sh](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/portable/linux-docker/start.sh) to your server
- change DOCKER_IMAGE variable with given docker image, for example ```DOCKER_IMAGE=apofig/codenjoy-a2048:2019-03-21```
- change SERVER_PORT variable (port is 8080 by default)
- check lines near ```cat <<EOT >> $HOME_DIR/config/codenjoy-contest.properties``` here script will update codenjoy properties file every time you run this script, so don't write your changes there - just update this config
    * change ```admin.password=adminPassword``` then go to ```http://your-server:8080/codenjoy-contest/admin``` and use admin/adminPassword for login
    * change ```server.ip=127.0.0.1``` to your server IP
    * change ```game.ai=true``` if you want that our default AI will play
    * change ```log.debug=true``` to enable logging DEBUG messages into ```./log/codenjoy-contest.log```
- then start this script by ```sudo bash start.sh```
- go to ```http://your-server:8080/codenjoy-contest/```
- codenjoy!          