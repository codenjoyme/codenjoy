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

if [ -x "$(command -v zip)" ]; then
    echo "[93mZip installed[0m" ;
else
    eval_echo "sudo apt-get install zip -y"
fi

eval_echo ". config.sh"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo "[93m"
echo "Work in: $DIR"
echo "[0m"

eval_echo "cd $DIR/logs && bash get-docker-compose-logs.sh"

eval_echo "docker-compose down --remove-orphans"
eval_echo "sudo chown -R ${USER:=$(/usr/bin/id -run)}:$USER ."
eval_echo "zip -r ./../codenjoy.zip ./*"
eval_echo "rm -rf ./*"

