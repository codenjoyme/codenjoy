mkdir ./docker/

now=`date +%Y-%m-%d_%H-%M-%S`
echo $now


#B# cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-balancer') ./docker/codenjoy-balancer-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-database') ./docker/codenjoy-database-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-contest') ./docker/codenjoy-contest-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' nginx') ./docker/nginx-$now.log
#B# cp $(eval 'docker inspect --format='{{.LogPath}}' codenjoy-balancer-frontend') ./docker/codenjoy-balancer-frontend-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' mycgi') ./docker/mycgi-$now.log
cp $(eval 'docker inspect --format='{{.LogPath}}' pgadmin') ./docker/pgadmin-$now.log

chown alex:alex ./docker/*

sudo gzip ./docker/*.log

