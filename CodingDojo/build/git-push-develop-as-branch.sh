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

show_branch_on_git() {
  url=https://github.com/codenjoyme/codenjoy/compare/develop%2E%2E%2E$BRANCH
	machine=$(check_machine)
    if [[ "$machine" == "Mac" || "$machine" == "Linux" ]]; then
        eval_echo "open $url"
    else
        eval_echo "explorer $url"
    fi
}

eval_echo "cd .."

eval_echo "`ssh-agent -s`"
eval_echo "ssh-add ~/.ssh/*_rsa"

eval_echo "CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)"

if [[ -n "$1" ]]; then
    BRANCH="$1"
    color "Branch '$BRANCH' is selected."
else
    color "Please enter branch name or press Enter for current branch '$CURRENT_BRANCH'"
    read BRANCH

    if [[ "$BRANCH" == "" ]]; then
        eval_echo "BRANCH=$CURRENT_BRANCH"
    fi
fi

if [[ -n "$2" ]]; then
    submodule_commit="$2"
    color "Submodule commit option '$submodule_commit' is selected."
else
    color "Do we need to commit submodules: y/n?"
    read submodule_commit
fi

if [[ "$submodule_commit" == "y" ]]; then
	eval_echo "git submodule foreach git push origin master"
fi

eval_echo "git checkout -B $BRANCH"
eval_echo "git pull origin $BRANCH"
eval_echo "git push origin $BRANCH"

if [[ "$BRANCH" != "$CURRENT_BRANCH" ]]; then
    eval_echo "git checkout develop"
    eval_echo "git branch -D $BRANCH"
fi

eval_echo "show_branch_on_git"

echo
color "Press Enter to continue"
read