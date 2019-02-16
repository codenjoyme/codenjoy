if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

cat database.sql | docker exec -i codenjoy-database psql -U codenjoy
