#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

echo "[92m========================================================================================================================"
echo "======================================================= Save logs ======================================================"
echo "========================================================================================================================[0m"

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

save_log() {
    container=$1
    time=$2

    count=$(docker ps -a | grep "$container" | wc -l)
    if (( $count > 0 )) ; then
        mkdir -p ./docker/$time/
        cp $(eval 'docker inspect --format='{{.LogPath}}' $container') ./docker/$time/$container-$time.log ;
        ls -la ./docker/$time/$container-$time.log ;
    else
        echo "Container '$container' not exists" ;
    fi
}

echo "[93m"
now=`date +%Y-%m-%d_%H-%M-%S`
echo $now
echo "[0m"

eval_echo "mkdir ./docker/"

eval_echo "save_log 'codenjoy-balancer' $now"
eval_echo "save_log 'codenjoy-database' $now"
eval_echo "save_log 'codenjoy-contest' $now"
eval_echo "save_log 'nginx' $now"
eval_echo "save_log 'codenjoy-balancer-frontend' $now"
eval_echo "save_log 'mycgi' $now"
eval_echo "save_log 'pgadmin' $now"

eval_echo "chown alex:alex ./docker/$now"

eval_echo "tar -zcvf ./docker/$now.tar.gz ./docker/$now && rm -R ./docker/$now"

eval_echo "ls -la ./docker/$now.tar.gz"

