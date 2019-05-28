#!/usr/bin/env sh

DB_DIR=/data/dojoserver

mkdir -p $DB_DIR

docker run \
        -d \
        --name dojorena-game-server \
        -e GAME_AI=true \
        -e SPRING_PROFILES_ACTIVE=sqlite,debug,icancode \
        -v $DB_DIR:/usr/app/database \
        -p 8080:8080 \
        dojorena/game-server:1.1.0
