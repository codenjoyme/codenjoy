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
eval_echo "sudo apt-get remove docker docker-engine docker.io containerd runc"
eval_echo "sudo apt-get -y update"
eval_echo "curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -"
eval_echo "sudo apt-key fingerprint 0EBFCD88"
eval_echo 'sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"'
eval_echo "sudo apt-get update -y"
eval_echo "sudo apt-get install docker-ce docker-ce-cli containerd.io"
eval_echo "sudo systemctl status docker --no-pager"
eval_echo "sudo usermod -aG docker $USER"
eval_echo "sudo docker -v"
	
# setup compose
eval_echo "curl -L 'https://github.com/docker/compose/releases/download/1.24.1/docker-compose-$(uname -s)-$(uname -m)' -o /usr/local/bin/docker-compose"
eval_echo "chmod +x /usr/local/bin/docker-compose"
eval_echo "docker-compose --version"
