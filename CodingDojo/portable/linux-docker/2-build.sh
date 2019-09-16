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

DOCKER_IMAGE=apofig/codenjoy-contest:1.1.1

eval_echo "docker build -f ./Dockerfile -t $DOCKER_IMAGE ."
