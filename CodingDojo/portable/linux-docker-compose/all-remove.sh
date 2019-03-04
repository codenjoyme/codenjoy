#!/usr/bin/env bash

# cd ./applications
# bash force-remove-all-images.sh
# cd ..

docker-compose -f docker-compose.yml -f codenjoy.yml -f wordpress.yml down

rm -rf ./applications
rm -rf ./config
rm -rf ./logs
rm -rf ./ssl-cert
rm ./*