#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

if [ -x "$(command -v docker)" ]; then
    echo "[93mDocker installed[0m" ;

    if [ -x "$(command -v docker-compose)" ]; then
        echo "[93mDocker comopose installed[0m" ;
        exit ;
    fi
fi

echo "[92m========================================================================================================================"
echo "================================================== Installing Docker ==================================================="
echo "========================================================================================================================[0m"

# setup docker
eval_echo "sudo apt-get update -y"
eval_echo "sudo apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys 58118E89F3A912897C070ADBF76221572C52609D"
eval_echo "sudo apt-get install software-properties-common -y"
eval_echo "sudo apt-get install apt-transport-https -y"
eval_echo "sudo apt-add-repository 'deb https://apt.dockerproject.org/repo ubuntu-xenial main'"
eval_echo "sudo apt-get update -y"
eval_echo "apt-cache policy docker-engine"
eval_echo "sudo apt-get install -y docker-engine"
eval_echo "sudo systemctl status docker --no-pager"
	
# setup compose
eval_echo "curl -L 'https://github.com/docker/compose/releases/download/1.23.1/docker-compose-$(uname -s)-$(uname -m)' -o /usr/local/bin/docker-compose"
eval_echo "chmod +x /usr/local/bin/docker-compose"
eval_echo "docker-compose --version"
