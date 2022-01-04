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

eval_echo "OUT=$(pwd)/out"
eval_echo "mkdir $OUT"
eval_echo "cd .."
eval_echo "PROJECT_ROOT=$(pwd)"
eval_echo "cd .."
eval_echo "GIT_ROOT=$(pwd)"
eval_echo "REPO=$GIT_ROOT/repo"

eval_echo "`ssh-agent -s`"
eval_echo "ssh-add ~/.ssh/*_rsa"

eval_echo "cd $PROJECT_ROOT"
eval_echo "./mvnw -f ./pom.xml -DaltDeploymentRepository=snapshots::default::file:./../repo/snapshots clean deploy -DskipTests=true -DgitDir=$GIT_ROOT 2>&1 | tee $OUT/snapshot-deploy.log" 

eval_echo "cd $REPO"

eval_echo "git add ."
eval_echo "git commit -m'New release'"
eval_echo "git push origin master"

eval_echo "cd $OUT/.."

echo Press Enter to continue
read