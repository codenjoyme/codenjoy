#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

eval_echo "git submodule foreach git pull origin master"
eval_echo "git add ."
eval_echo "git commit -m'New release'"
eval_echo "git push origin master"

echo Press any key to continue
read