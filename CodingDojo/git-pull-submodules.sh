#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

eval_echo "ssh-agent -s"
eval_echo "ssh-add ~/.ssh/*_rsa"

eval_echo "BRANCH=$(git rev-parse --abbrev-ref HEAD)"
eval_echo "git pull origin develop"
eval_echo "git pull origin $BRANCH"
eval_echo "git submodule foreach git pull origin master"

echo Press any key to continue
read