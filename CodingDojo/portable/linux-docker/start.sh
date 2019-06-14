#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]; then
    echo "[91mPlease run as root on the /srv/codenjoy folder[0m" ;
    exit ;
fi

DOCKER_IMAGE=apofig/codenjoy-contest:1.1.0
SERVER_PORT=8080

JETTY_PID=999

HOME_DIR=$(
  cd $(dirname "$0")
  pwd
)
echo $HOME_DIR

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

eval_echo "mkdir $HOME_DIR/logs"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/logs"

eval_echo "touch $HOME_DIR/logs/codenjoy-contest.log"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/logs/codenjoy-contest.log"

eval_echo "mkdir $HOME_DIR/config"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/config"

eval_echo "rm $HOME_DIR/config/codenjoy-contest.properties"
cat <<EOT >> $HOME_DIR/config/codenjoy-contest.properties
game.save.auto=true
board.save.ticks=1
game.ai=false
log.debug=false
page.main.url=
page.registration.url=
page.registration.nickname=false
page.help.language=
email.verification=false
email.password=
email.name=info@codenjoy.com
donate.code=
database.url=
database.name=
database.user=
database.password=
admin.password=admin
email.hash=secureHash
server.ip=127.0.0.1
EOT

eval_echo "mkdir $HOME_DIR/database"
eval_echo "chown $JETTY_PID:$JETTY_PID $HOME_DIR/database"

eval_echo "docker rm --force codenjoy-contest"

eval_echo "docker run --name codenjoy-contest -p $SERVER_PORT:8080 -v $HOME_DIR/database:/var/lib/jetty/database -v $HOME_DIR/config:/var/lib/jetty/config -v $HOME_DIR/logs/codenjoy-contest.log:/var/lib/jetty/logs/codenjoy-contest.log -d $DOCKER_IMAGE"