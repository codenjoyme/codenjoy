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

GIT_REPO=https://github.com/codenjoyme/codenjoy.git
GIT_REVISION=master

eval_echo "git clone $GIT_REPO"
eval_echo "git checkout $GIT_REVISION"