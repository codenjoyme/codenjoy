#!/usr/bin/env bash

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2012 - 2022 Codenjoy
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
# #L%
###

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
    eval_echo "git submodule add --force https://github.com/codenjoyme/codenjoy-$GAME.git ./CodingDojo/games/$GAME"
	eval_echo "git add ."
	eval_echo "git commit -m '[$GAME] Added game as submodule.'"
}

eval_echo "cd ../.."
ROOT=$(pwd)

while true
do
    color "Please enter game name"
    read GAME

    if [[ "$GAME" == "" ]]; then
        color "Game is empty"
        continue
    fi

    if [[ ! -d "$ROOT/CodingDojo/games/$GAME" ]]; then
        color "Game not exists"
        continue
    fi

    eval_echo "git_add_game_submodule '$GAME'"
done

color "Press Enter to continue"
read