#!/usr/bin/env bash

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

eval_echo_color_output() {
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
    eval_echo_color $CL_COMMAND "$command"
}

eval_echo_color() {
    color=$1
    command=$2
    color $color "$command"
    echo

    eval $command
}

ask() {
    ask_message $CL_QUESTION "Press any key to continue"
}

ask_result=""
ask_message() {
    color=$1
    message=$2
    color $color "$message"
    read ask_result
}

sep() {
    color $CL_COMMAND "---------------------------------------------------------------------------------------"
}

build_args=""
read_env() {
    for entry in $(cat $ROOT/.env)
    do
        if [[ ! $entry == \#* ]]
        then
            export $entry
            
            build_args+=" --build-arg $entry"
            
            color $CL_INFO "$entry"          
        fi
    done
}

color $CL_HEADER "Setup variables..."
echo

    eval_echo "ROOT=$PWD"

    read_env

color $CL_HEADER "Installing docker..."
echo

    ask_message $CL_QUESTION "There is a need to update the system and install docker. Should we install (y/n)?"
    if [[ "$ask_result" == "y" ]]; then
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

        eval_echo_color_output "docker -v"
    else
        color $CL_INFO "Skipped"
    fi

color $CL_HEADER "Building client..."
echo
       
    eval_echo "DOCKER_BUILDKIT=1 docker build -t client-server -f Dockerfile ./ $build_args"

color $CL_HEADER "Starting client..."
echo

    eval_echo "docker container rm client-server --force"

    eval_echo "docker run --name client-server -d client-server"

    eval_echo "docker logs --follow client-server"

ask