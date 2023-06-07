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

eval_echo "cd ../../.git"

from=https://github.com/
to=git@github.com:

# because of different sed versions on different platforms (macos, linux)
# we need to use different sed commands
if [[ "$OSTYPE" == "darwin"* ]]; then
    platform_diff="''"
else
    platform_diff=""
fi

# iterate through all submodules
while read -r line; do
    if [[ "$line" =~ ^\[submodule.* ]]; then
        path=$(echo $line | awk -F '"' '{print $2}')
        submodule_path="modules/$path"
        color "Processing submodule: $submodule_path"

        # navigate to submodule path and modify config file
        eval_echo "cd $submodule_path"
        eval_echo "cat config"
        eval_echo "sed -i $platform_diff 's#url = $from#url = $to#g' config"
        eval_echo "cat config"
        eval_echo "cd -"
    fi
done < config

echo
color "Press Enter to continue"
read