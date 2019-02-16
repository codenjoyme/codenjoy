if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

mkdir ./docker/

now=`date +%Y-%m-%d_%H-%M-%S`
echo $now

cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-balancer') ./docker/codenjoy-balancer-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-database') ./docker/codenjoy-database-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-contest') ./docker/codenjoy-contest-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' nginx') ./docker/nginx-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-balancer-frontend') ./docker/codenjoy-balancer-frontend-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' mycgi') ./docker/mycgi-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' pgadmin') ./docker/pgadmin-$now.log

chown alex:alex ./docker/*

sudo gzip ./docker/*.log

