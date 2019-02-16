#!/usr/bin/env bash

if [ "x$CONFIG" = "x" ]; then
    . config.sh ;
fi

docker-compose down

if [ "x$OPEN_PORTS" = "xtrue" ]; then
    echo "[93mWARNING: All applications ports are exposed out of containers[0m"

    docker-compose -f docker-compose.yml -f open-ports.yml up -d codenjoy_db
    sleep 10
    docker-compose -f docker-compose.yml -f open-ports.yml up -d
else
    docker-compose up -d codenjoy_db
    sleep 10
    docker-compose up -d
fi

