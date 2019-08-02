Linux portable script (simple version)
======================

How to run server on Linux?
----------------------------
- copy this sctipt [https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/portable/linux-docker/start.sh](https://github.com/codenjoyme/codenjoy/blob/master/CodingDojo/portable/linux-docker/start.sh) to your server (in the `/srv/codenjoy` folder for exapmple)
- with `1-git-clone.sh` 
    * [optional] change `GIT_REPO=https://github.com/codenjoyme/codenjoy.git`
    * [optional] change `GIT_REVISION=master`
    * run it to clone project `sudo bash 1-git-clone.sh`
- with `2-build.sh` 
    * [optional] change `DOCKER_IMAGE=apofig/codenjoy-contest:1.1.0`
    * run it to build project and create image `sudo bash 2-build.sh` 
- with `3-start.sh`
    * [optional] change `DOCKER_IMAGE=apofig/codenjoy-contest:1.1.0`
    * [optional] select `SERVER_PORT=8080`
    * [optional] change `PROFILES=sqlite,icancode`
    * [optional] change `GAME_AI=false` to disable default AI with first player
    * [optional] change docker container name `CONTAINER_NAME=codenjoy`
    * [optional] change application context `CONTEXT=codenjoy-contest`
    * run it to start server `sudo bash 3-start.sh`
- go to ```http://your-server:8080/codenjoy-contest/```
- codenjoy!    