#!/bin/bash
HOST="root@jschallenge.vreshch.com"

echo 'JS challenge: deploy the project via ssh';

curl "http://79.143.176.13:1234/run?password=${CI_DEPLOY_PWD}&command=server.rebuild";
