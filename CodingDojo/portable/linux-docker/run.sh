#!/usr/bin/env bash

DB_DIR=/data/dojoserver
PROFILES=sqlite
GAME_AI=false
FORCE=false
CONTAINER_NAME=dojorena-game-server

mkdir -p $DB_DIR

function usage() {
    echo ""
    echo "Usage:"
    echo ""
    echo "$0 [OPTIONS]"
    echo "--sso                 - use OAuth2 authorization with telescopeai as an Authorization Server"
    echo "-f, --force           - forcefully kill already running instance if any"
    echo "-v, --debug           - turn off JS minification"
    echo "--ai                  - turn in AI bots for all the games except icancode as it's not supported"
    echo "--help                - usage"
    exit 1
}

while true; do
    case "$1" in
        --sso) SSO=true; shift ;;
        -v | --debug) PROFILES="$PROFILES,debug"; shift ;;
        -f | --force) FORCE=true; shift ;;
        --ai) GAME_AI=true; shift ;; 
        --help) usage ;;
        *) break ;;
    esac
done

if [ "$FORCE" = true ]; then
    docker rm -f $CONTAINER_NAME 2>/dev/null
fi 

docker run \
        -d \
        --name $CONTAINER_NAME \
        -e GAME_AI=$GAME_AI \
        -e SPRING_PROFILES_ACTIVE=$PROFILES \
        -v $DB_DIR:/usr/app/database \
        -p 8080:8080 \
        dojorena/game-server:1.1.1
