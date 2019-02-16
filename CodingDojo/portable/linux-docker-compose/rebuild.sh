if [ "$EUID" -ne 0 ]
  then echo "Please run as root"
  exit
fi

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo $DIR

bash init-structure.sh

# TODO continue with db backup
# cd $DIR/content
# bash backup.sh

cd $DIR/logs
bash get-docker-compose-logs.sh

cd $DIR/applications
bash build.sh

docker container rm $(docker ps -a | grep -v "mycgi" | cut -d ' ' -f1) --force
# TODO this is balancer frontend, merge it in codenjoy git repo
# docker rmi vreshch/codenjoy-lb
docker rmi apofig/codenjoy-contest:1.0.28 --force
docker rmi apofig/codenjoy-balancer:1.0.28 --force

echo "[92m========================================================================================================================"
echo "================================================ Docker compose starting ==============================================="
echo "========================================================================================================================[0m"

cd $DIR
sudo bash init-structure.sh
sudo docker-compose build --no-cache
bash up.sh  

echo "[92m========================================================================================================================"
echo "========================================================= DONE ========================================================="
echo "========================================================================================================================[0m"

cd $DIR/applications
bash check-revision.sh