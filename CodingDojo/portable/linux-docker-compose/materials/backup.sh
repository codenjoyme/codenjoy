#!/usr/bin/env bash
if [ "$EUID" -ne 0 ]
  then echo "[91mPlease run as root[0m"
  exit
fi

file=dump_`date +%Y-%m-%d"_"%H_%M_%S`.sql
docker exec -t codenjoy-database pg_dumpall -c -U codenjoy > $file
gzip $file

