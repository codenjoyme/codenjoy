#!/usr/bin/env bash

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

eval_echo "cd CodingDojo/portable/linux-docker-compose"
eval_echo "git status"
eval_echo "cd ../../.."
eval_echo "git status"