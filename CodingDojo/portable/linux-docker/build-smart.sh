GIT_REPO=https://github.com/codenjoyme/codenjoy.git
REVISION=snake-new-ui
CODENJOY_CONTEXT=codenjoy-contest
GAME=snakebattle
SKIP_TEST=true

eval_echo() {
    to_run=$1
    echo $to_run
    eval $to_run
}

rm -rf ./logs
mkdir ./logs

# checkout if needed
if [[ "$(docker images -q codenjoy-source 2> /dev/null)" == "" ]]; then
    echo "========================================================================================================================"
    echo"======================================== Codenjoy sources not found. Checking out ======================================="
    echo "========================================================================================================================"

    # remove everything
    docker rmi codenjoy-deploy --force
    docker rmi codenjoy-source --force
    docker rmi java-workspace --force

    # build workspace
    docker build --target javaworkspace -t java-workspace . |& tee ./logs/java-workspace.log

    # checkout and builf project
    docker build --target codenjoysource -t codenjoy-source . --build-arg GIT_REPO=${GIT_REPO} --build-arg REVISION=${REVISION} --build-arg SKIP_TESTS=${SKIP_TESTS} |& tee ./logs/codenjoy-source.log
fi

echo "========================================================================================================================"
echo "=============================================== Checking out last version =============================================="
echo "========================================================================================================================"

docker container rm temp --force
docker run --name temp -d codenjoy-source
eval_echo "docker exec -it temp bash -c 'cd /tmp/codenjoy && git checkout ${REVISION} && git pull origin'" 

echo "========================================================================================================================"
echo "=============================================== Building codenjoy server ==============================================="
echo "========================================================================================================================"

if [ "x$GAME" = "x" ] ; 
then 
    eval_echo "docker exec -it temp bash -c 'cd /tmp/codenjoy/CodingDojo/builder && mvn clean install -Dcontext=${CODENJOY_CONTEXT} -DallGames' |& tee ./logs/codenjoy-deploy.log" ;
else
    eval_echo "docker exec -it temp bash -c 'cd /tmp/codenjoy/CodingDojo/builder && mvn clean install -Dcontext=${CODENJOY_CONTEXT} -P$GAME' |& tee ./logs/codenjoy-deploy.log" ;
fi
docker cp temp:/tmp/codenjoy/CodingDojo/builder/target/${CODENJOY_CONTEXT}.war ./${CODENJOY_CONTEXT}.war

echo "========================================================================================================================"
echo "=============================================== Building balancer server ==============================================="
echo "========================================================================================================================"

eval_echo "docker exec -it temp bash -c 'cd /tmp/codenjoy/CodingDojo/balancer && mvn clean install -DskipTests=${SKIP_TESTS}' |& tee ./logs/balancer-deploy.log"
docker cp temp:/tmp/codenjoy/CodingDojo/balancer/target/codenjoy-balancer.war ./codenjoy-balancer.war

echo "========================================================================================================================"
echo "====================================================== Almost here ====================================================="
echo "========================================================================================================================"

docker commit temp codenjoy-source
docker container rm temp --force

sudo docker build --target codenjoyserver -t codenjoy-contest . # --build-arg CONTEXT=codenjoy-contest
sudo docker build --target balancerserver -t codenjoy-balancer .
sudo docker run --name codenjoy-contest -d -p 80:8080 codenjoy-contest
sudo docker run --name codenjoy-balancer -d -p 81:8080 codenjoy-balancer

echo "========================================================================================================================"
echo "========================================================== DONE ========================================================"
echo "========================================================================================================================"
