docker container rm temp --force &> /dev/null
docker run --name temp -d codenjoy-source &> /dev/null

docker exec temp bash -c 'cd /tmp/codenjoy && git rev-parse HEAD'

docker container rm temp --force &> /dev/null
