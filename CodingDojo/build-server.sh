#!/bin/bash

export ROOT=$PWD

if [ "$GAMES_TO_RUN" == "" ] ; then
	# export GAMES_TO_RUN=all
	export GAMES_TO_RUN=mollymage
	# export GAMES_TO_RUN=tetris,snake,mollymage
fi

echo Building server with [$GAMES_TO_RUN]

if [ "$GAMES_TO_RUN" == "all" ] ; then
    "$ROOT/mvnw" clean install -DskipTests
    
    cd "$ROOT/server" || return
    "$ROOT/mvnw" clean package -DskipTests -DallGames
else
    cd "$ROOT/games" || return
     "$ROOT/mvnw" clean install -N
    
    cd "$ROOT/games/engine" || return
    "$ROOT/mvnw" clean install -DskipTests
    
    for GAME in ${GAMES_TO_RUN//" "/"",""} ; do
        cd "$ROOT/games/$GAME" || return
        "$ROOT/mvnw" clean install -DskipTests
    done
    
    cd "$ROOT/server" || return
    "$ROOT/mvnw" clean package -DskipTests -P$GAMES_TO_RUN
fi

cd "$ROOT" || return
