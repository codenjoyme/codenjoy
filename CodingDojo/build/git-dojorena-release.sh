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

eval_echo "`ssh-agent -s`"
eval_echo "ssh-add ~/.ssh/*_rsa"

# merge current branch to dojorena-release branch and push it to origin

current_branch=`git branch --show-current`
eval_echo "git checkout dojorena-release"
eval_echo "git pull origin dojorena-release"
eval_echo "git merge $current_branch"
eval_echo "git push origin dojorena-release"
eval_echo "git checkout $current_branch"

color "Do you want to create tag (Y/N)?"
read answer
if [[ "$answer" == "Y" ]]; then
  TAG=`date +%Y-%m-%d`_dojorena-release
  eval_echo "git tag -d $TAG"
  eval_echo "git tag $TAG"
  eval_echo "git push origin $TAG"
  eval_echo "git tag"
fi

echo
color "Press Enter to continue"
read