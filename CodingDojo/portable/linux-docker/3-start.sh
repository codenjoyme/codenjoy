#!/usr/bin/env bash

if [ "$EUID" -ne 0 ]; then
    echo "[91mPlease run as root on the /srv/codenjoy folder[0m" ;
    exit ;
fi

HOME_DIR=$(
  cd $(dirname "$0")
  pwd
)
echo $HOME_DIR

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

DOCKER_IMAGE=apofig/codenjoy-contest:1.1.0
CONTEXT=/codenjoy-contest
SERVER_PORT=8080
PROFILES=sqlite,icancode
GAME_AI=true
ADMIN_PASSWORD=admin
CONTAINER_NAME=codenjoy

eval_echo "mkdir $HOME_DIR/logs"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/logs"

eval_echo "touch $HOME_DIR/logs/codenjoy-contest.log"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/logs/codenjoy-contest.log"

eval_echo "mkdir $HOME_DIR/database"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/database"

eval_echo "docker rm --force $CONTAINER_NAME"

eval_echo "docker run -d --name $CONTAINER_NAME -e GAME_AI=$GAME_AI -e ADMIN_PASSWORD=$ADMIN_PASSWORD -e CONTEXT=$CONTEXT -e SPRING_PROFILES_ACTIVE=$PROFILES -v $HOME_DIR/database:/usr/app/database -p $SERVER_PORT:8080 $DOCKER_IMAGE"

eval_echo "docker attach $CONTAINER_NAME"
