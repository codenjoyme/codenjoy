docker exec -t codenjoy-database pg_dumpall -c -U codenjoy > dump_`date +%Y-%m-%d"_"%H_%M_%S`.sql
