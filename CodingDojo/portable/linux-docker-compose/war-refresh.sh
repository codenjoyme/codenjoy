#!/usr/bin/env bash
docker-compose down

docker-compose build --no-cache

bash up.sh