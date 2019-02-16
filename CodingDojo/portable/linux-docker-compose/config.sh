#!/usr/bin/env bash
BUILD_SERVER=true
BUILD_BALANCER=true
GIT_REPO=https://github.com/codenjoyme/codenjoy.git
REVISION=master
CODENJOY_CONTEXT=codenjoy-contest
GAME=snakebattle
GAME_PROJECT=snake-battle

BASIC_AUTH_LOGIN=codenjoy
BASIC_AUTH_PASSWORD=secureBasicAuthPassword

echo "[93m"
echo "BUILD_SERVER=$BUILD_SERVER"
echo "BUILD_BALANCER=$BUILD_BALANCER"
echo "GIT_REPO=$GIT_REPO"
echo "REVISION=$REVISION"
echo "GAME=$GAME"
echo "GAME_PROJECT=$GAME_PROJECT"
echo "BASIC_AUTH_LOGIN=$BASIC_AUTH_LOGIN"
echo "BASIC_AUTH_PASSWORD=$BASIC_AUTH_PASSWORD"
echo "[0m"