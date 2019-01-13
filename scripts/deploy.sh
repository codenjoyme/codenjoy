#!/bin/bash
HOST="root@jschallenge.vreshch.com"

echo 'JS challenge: deploy the project via ssh';

ssh ${HOST} <<EOF
  docker login -u ${CI_REGISTRY_USER} -p ${CI_REGISTRY_PASSWORD} && docker pull vreshch/codenjoy-lb
  docker stop codenjoy-lb || true
  docker rm  codenjoy-lb || true
  docker run -p 8080:80 -d --restart unless-stopped --name codenjoy-lb vreshch/codenjoy-lb
echo 'Success: codenjoy-lb deployed';
