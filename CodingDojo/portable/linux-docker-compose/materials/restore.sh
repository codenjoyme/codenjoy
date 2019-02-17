#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

cat database.sql | docker exec -i codenjoy-database psql -U codenjoy
