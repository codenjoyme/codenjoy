#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

echo "[92m========================================================================================================================"
echo "========================================================= START ========================================================"
echo "========================================================================================================================[0m"

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo "[93m"
echo "Work in: $DIR"
echo "[0m"

eval_echo ". config.sh"

eval_echo "bash init-structure.sh"

# TODO continue with db backup
# eval_echo "cd $DIR/materials && bash backup.sh"

eval_echo "cd $DIR/logs && bash get-docker-compose-logs.sh"

eval_echo "cd $DIR/applications && bash build.sh"

eval_echo "docker container rm $(docker ps -a | grep -v 'CONTAINER' | cut -d ' ' -f1) --force"
# TODO this is balancer frontend, merge it in codenjoy git repo
# eval_echo "docker rmi vreshch/codenjoy-lb"
eval_echo "docker rmi apofig/codenjoy-contest:1.1.0 --force"
eval_echo "docker rmi apofig/codenjoy-balancer:1.1.0 --force"

echo "[92m========================================================================================================================"
echo "================================================ Docker compose starting ==============================================="
echo "========================================================================================================================[0m"

cd $DIR
eval_echo "docker-compose build --no-cache"
eval_echo "bash up.sh"

echo "[92m========================================================================================================================"
echo "========================================================= DONE ========================================================="
echo "========================================================================================================================[0m"

eval_echo "cd $DIR/applications && bash check-revision.sh"