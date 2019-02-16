if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

file=dump_`date +%Y-%m-%d"_"%H_%M_%S`.sql
docker exec -t codenjoy-database pg_dumpall -c -U codenjoy > $file
gzip $file

