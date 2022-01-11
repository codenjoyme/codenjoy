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

check_machine() {
    unameOut="$(uname -s)"
    case "${unameOut}" in
        Linux*)     machine=Linux;;
        Darwin*)    machine=Mac;;
        CYGWIN*)    machine=Cygwin;;
        MINGW*)     machine=MinGw;;
        *)          machine="UNKNOWN:${unameOut}"
    esac
    echo ${machine}
}

parameter_prefix() {
    machine=$(check_machine)
    if [[ "$machine" == "Mac" || "$machine" == "Linux" ]]; then
        echo --
    else
        echo -D
    fi
}

eval_echo "from=1.1.1"

color "Please enter new version for '$from'"
read to

if [[ "$to" == "" ]]; then
    eval_echo "to=$from"
fi

eval_echo "ROOT=$(pwd)/.."

eval_echo "cd $ROOT/utilities"
eval_echo "preffix=$(parameter_prefix)"
eval_echo "../mvnw -f ./pom.xml clean compile -Pchange-version ${preffix}replace.from=$from ${preffix}replace.to=$to"

eval_echo "cd $ROOT"

color "Press Enter to continue"
read