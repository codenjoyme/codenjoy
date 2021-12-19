#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

LANGUAGE=python
cd ./../$LANGUAGE/build
bash run.sh

echo Press Enter to continue
read