#!/usr/bin/env bash

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

echo "[93m"
for entry in $(cat ./.env)
do
  if [[ ! $entry == \#* ]]
  then
    export $entry
    echo "$entry"
  fi
done
echo "[0m"

eval_echo "sed -i 's/\(server_name \).*\$/\1$SERVER_IP;/' ./config/nginx/domain.conf"
cat ./config/nginx/domain.conf | grep 'server_name '

if [ "x$BALANCER" = "xtrue" ]; then
    domain=$BALANCER_DOMAIN
else
    domain=$CODENJOY_DOMAIN
fi

eval_echo "sed -i 's,\(return 301 https\?://\).*\\$,\1$domain\$,' ./config/nginx/domain.conf"
cat ./config/nginx/domain.conf | grep 'return 301 '

eval_echo "sed -i 's/\(server_name \).*\$/\1$BALANCER_DOMAIN;/' ./config/nginx/codenjoy-balancer.conf"
cat ./config/nginx/codenjoy-balancer.conf | grep 'server_name '

eval_echo "sed -i 's/\(server_name \).*\$/\1$CODENJOY_DOMAIN;/' ./config/nginx/codenjoy-contest.conf"
cat ./config/nginx/codenjoy-contest.conf | grep 'server_name '

comment() {
    file=$1
    marker=$2
    flag=$3
    if [ "x$flag" = "xtrue" ]; then
        eval_echo "sed -i '/$marker/s/^#\+//' $file"
    else
        eval_echo "sed -i '/$marker/s/^#\?/#/' $file"
    fi
    cat $file | grep $marker
}

ports() {
    file=$1
    comment $file "#P#" $OPEN_PORTS
}

ports ./docker-compose.yml
ports ./codenjoy.yml
ports ./balancer.yml

basic_auth() {
    file=$1
    comment $file "#A#" $BASIC_AUTH
}

basic_auth ./config/nginx/codenjoy-balancer.conf
basic_auth ./config/nginx/codenjoy-contest.conf

ssl() {
    file=$1
    comment $file "#S#" $SSL
    if [ "x$SSL" = "xtrue" ]; then
        NOT_SSL="false";
    else
        NOT_SSL="true";
    fi
    comment $file "#!S#" $NOT_SSL
}

ssl ./config/nginx/domain.conf
ssl ./config/nginx/codenjoy-balancer.conf
ssl ./config/nginx/codenjoy-contest.conf
