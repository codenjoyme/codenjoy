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

# red 91
# green 92
# yellow 93
# blue 94
# pink 95
# light blue 96
# purple 97
CL_HEADER=92
CL_COMMAND=94
CL_QUESTION=95
CL_INFO=93

eval_echo_color() {
    command=$1
    output=$($command)
    color $CL_COMMAND "$command"
    color $CL_INFO "$output"
}

color() {
    color=$1
    message=$2
    echo "[${color}m$message[0m"
}

eval_echo() {
    command=$1
    color $CL_COMMAND "$command"
    echo
    eval $command
}

ask() {
    ask_message $CL_QUESTION "Press Enter to continue"
}

ASK_RESULT=""
ask_message() {
    color=$1
    message=$2
    color $color "$message"
    read ASK_RESULT
}

sep() {
    color $CL_COMMAND "---------------------------------------------------------------------------------------"
}

export_entry() {
    entry=$1
    export $entry
    # don't add LANGUAGE env variable to the BUILD_ARGS
    if [[ ! "$entry" == *"LANGUAGE"* ]]; then
        BUILD_ARGS=$(echo "$BUILD_ARGS --build-arg $entry")
    fi
    color $CL_INFO "$entry"
}

BUILD_ARGS=""
read_env() {
    for entry in $(cat $ROOT/.env); do
        if [[ ! $entry == \#* ]]; then
            export_entry $entry
        fi
    done
}

color $CL_HEADER "Setup variables..."
echo
    eval_echo "ROOT=$PWD/.."
    read_env
    color $CL_INFO "BUILD_ARGS=$BUILD_ARGS"

color $CL_HEADER "Installing docker..."
echo
    ask_message $CL_QUESTION "There is a need to update the system and install docker. Should we install (y/n)?"
    if [[ "$ASK_RESULT" == "y" ]]; then
        if [ "$EUID" -ne 0 ]; then
          color $COLOR5 "Please run as root"
          exit
        fi

        # setup docker
        eval_echo "apt-get remove docker docker-engine docker.io containerd runc"
        eval_echo "apt-get -y update"
        eval_echo "curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -"
        eval_echo "apt-key fingerprint 0EBFCD88"

        # for debian alpine
        # eval_echo 'apt-get install -y software-properties-common'
        # eval_echo 'add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(dpkg --status tzdata|grep Provides|cut -f2 -d'-') stable"'

        # for ubuntu
        eval_echo 'add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"'

        eval_echo "apt-get update -y"
        eval_echo "apt-get install docker-ce docker-ce-cli containerd.io -y"
        eval_echo "systemctl status docker --no-pager"
        eval_echo "usermod -aG docker $USER"

        eval_echo_color "docker -v"
    else
        color $CL_INFO "Skipped"
    fi

color $CL_HEADER "Building client..."
echo
    eval_echo "DOCKER_BUILDKIT=1 docker build -t client-server -f $ROOT/Dockerfile $ROOT/ $BUILD_ARGS"

color $CL_HEADER "Starting client..."
echo
    eval_echo "docker container rm client-server --force"
    eval_echo "docker run --name client-server -d client-server"
    eval_echo "docker logs --follow client-server"

ask