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

BLUE=94
GRAY=89
YELLOW=93

color() {
    message=$1
    [[ "$2" == "" ]] && color=$YELLOW || color=$2
    echo "[${color}m${message}[0m"
}

eval_echo() {
    command=$1
    [[ "$2" == "" ]] && color=$BLUE || color=$2
    color "${command}" $color
    echo
    eval $command
}

copy_from_game() {
   game=$1
   from=$ROOT/../games/$game/src/main/webapp/resources/$game/sprite
   to=$ROOT/java-script/games/$game/sprites
   eval_echo "rm -rf $to" $GRAY
   eval_echo "mkdir $to" $GRAY
   eval_echo "cp $from/*.* $to" $GRAY
}

eval_echo "ROOT=$(pwd)/.."
eval_echo "copy_from_game 'clifford'"
eval_echo "copy_from_game 'mollymage'"
eval_echo "copy_from_game 'sample'"
eval_echo "copy_from_game 'verland'"
eval_echo "copy_from_game 'rawelbbub'"

echo
color "Press Enter to continue"
read