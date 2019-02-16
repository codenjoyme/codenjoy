#!/usr/bin/env bash
docker container rm temp --force &> /dev/null
docker run --name temp -d codenjoy-source &> /dev/null

echo "[93m"
echo "GIT REVISION IS:"
docker exec temp bash -c 'cd /tmp/codenjoy && git rev-parse HEAD'
echo "[0m"

docker container rm temp --force &> /dev/null
