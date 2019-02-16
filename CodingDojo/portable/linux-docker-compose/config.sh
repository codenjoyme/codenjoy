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

eval_echo "sed -i 's/\(server_name \).*\$/\1$BALANCER_DOMAIN;/' ./config/nginx/codenjoy-balancer.conf"
cat ./config/nginx/codenjoy-balancer.conf | grep 'server_name '

eval_echo "sed -i 's/\(server_name \).*\$/\1$CODENJOY_DOMAIN;/' ./config/nginx/codenjoy-contest.conf"
cat ./config/nginx/codenjoy-contest.conf | grep 'server_name '