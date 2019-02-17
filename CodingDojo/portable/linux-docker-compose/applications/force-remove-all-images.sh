#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

eval_echo "docker image ls"
eval_echo "docker network ls"
eval_echo "docker container ps -a"

eval_echo "docker stop $(docker ps -aq)"
eval_echo "docker system prune --all --force"

eval_echo "docker image ls"
eval_echo "docker network ls"
eval_echo "docker container ps -a"