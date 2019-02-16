#!/usr/bin/env bash

if [ "x$CONFIG" = "x" ]; then
    . config.sh ;
fi

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

eval_echo "docker-compose down --remove-orphans"

if [ "x$PGADMIN" = "xtrue" ]; then
    echo "[93mWARNING: PGAdmin will start on port $PGADMIN_PORT[0m"
    pgadmin="-f pgadmin.yml"
fi

if [ "x$BALANCER" = "xtrue" ]; then
    echo "[93mBalancer will start[0m"
    balancer="-f balancer.yml"
fi

if [ "x$CODENJOY" = "xtrue" ]; then
    echo "[93mCodenjoy will start[0m"
    codenjoy="-f codenjoy.yml"
fi

eval_echo "docker-compose -f docker-compose.yml $balancer $codenjoy $pgadmin up -d codenjoy_db"
sleep 10
eval_echo "docker-compose -f docker-compose.yml $balancer $codenjoy $pgadmin up -d"

