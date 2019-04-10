#!/usr/bin/env bash

if [ "x$CONFIG" = "x" ]; then
    . config.sh ;
fi

service=$1

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

if [ "x$PGADMIN" = "xtrue" ]; then
    pgadmin="-f pgadmin.yml"
fi

if [ "x$BALANCER" = "xtrue" ]; then
    balancer="-f balancer.yml"
fi

if [ "x$CODENJOY" = "xtrue" ]; then
    codenjoy="-f codenjoy.yml"
fi

if [ "x$WORDPRESS" = "xtrue" ]; then
    wordpress="-f wordpress.yml"
fi

eval_echo "docker-compose -f docker-compose.yml $balancer $codenjoy $wordpress $pgadmin logs $service"