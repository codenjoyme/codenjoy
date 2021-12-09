#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

echo Please enter branch name
read BRANCH

eval_echo "git submodule foreach git push origin master"
eval_echo "git checkout -B $BRANCH"
eval_echo "git push origin $BRANCH"
eval_echo "git checkout develop"
eval_echo "git branch -D $BRANCH"

echo Press any key to continue
read