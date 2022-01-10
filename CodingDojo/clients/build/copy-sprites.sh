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

copy_to() {
   game=$1
   mode=$3
   from=$ROOT/games/$game/src/main/webapp/resources/$game/sprite/$3
   to=$2
   eval_echo "rm -rf $to" $GRAY
   eval_echo "mkdir $to" $GRAY
   eval_echo "cp $from/*.* $to" $GRAY
}

copy_to_javascript_client() {
   game=$1
   mode=$2
   to=$ROOT/clients/java-script/games/$game/sprites
   copy_to "$game" "$to"
}

copy_to_balancer() {
   game=$1
   mode=$2
   to=$ROOT/balancer-frontend/src/games/$game/images/sprite
   copy_to "$game" "$to" "$mode"
}

eval_echo "ROOT=$(pwd)/../.."

eval_echo "copy_to_javascript_client 'clifford'"
eval_echo "copy_to_javascript_client 'mollymage'"
eval_echo "copy_to_javascript_client 'sample'"
eval_echo "copy_to_javascript_client 'verland'"
eval_echo "copy_to_javascript_client 'rawelbbub'"

eval_echo "copy_to_balancer 'rawelbbub'"
eval_echo "copy_to_balancer 'mollymage'"
eval_echo "copy_to_balancer 'icancode' 'robot'"

echo
color "Press Enter to continue"
read