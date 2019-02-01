DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo $DIR

bash init-structure.sh

cd $DIR/applications
bash build.sh

docker container rm codenjoy-contest --force
docker container rm codenjoy-database --force
docker container rm nginx --force
docker rmi apofig/codenjoy-contest:1.0.28 --force
docker rmi nginx --force

echo "========================================================================================================================"
echo "================================================ Docker compose starting ==============================================="
echo "========================================================================================================================"

cd $DIR
sudo bash init-structure.sh
sudo docker-compose build --no-cache
sudo docker-compose up -d codenjoy_db
sleep 10
sudo docker-compose up -d 

echo "========================================================================================================================"
echo "========================================================= DONE ========================================================="
echo "========================================================================================================================"
