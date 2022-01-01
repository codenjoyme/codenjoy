#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

color() {
    message=$1
    echo "[93m$message[0m"
}

git_add_game_submodule() {
    GAME=$1
    eval_echo "rm -rf $ROOT/CodingDojo/games/$GAME"
	eval_echo "git add ."
	eval_echo "git commit -m '[$GAME] Removed game.'"
    eval_echo "git submodule add --force https://github.com/codenjoyme/codenjoy-$GAME.git $ROOT/CodingDojo/games/$GAME"
	eval_echo "git add ."
	eval_echo "git commit -m '[$GAME] Added game as submodule.'"
}

eval_echo "cd ../.."
ROOT=$(pwd)

while true
do
    color "Please enter game name"
    read GAME

    if [[ ! -d "$ROOT/CodingDojo/games/$GAME" ]]; then
        color "Game not exists"
        continue
    fi

    eval_echo "git_add_game_submodule '$GAME'"
done

color "Press Enter to continue"
read