GIT_REPO=https://github.com/codenjoyme/codenjoy.git
REVISION=snake-new-ui
CODENJOY_CONTEXT=codenjoy-contest
GAME=snakebattle

# TODO to remove t  his difference
GAME_PROJECT=snake-battle 

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
    docker build --target codenjoysource -t codenjoy-source . --build-arg GIT_REPO=${GIT_REPO} --build-arg REVISION=${REVISION} --build-arg SKIP_TESTS=true |& tee ./logs/codenjoy-source.log
fi

echo "========================================================================================================================"
echo "=============================================== Checking out last version =============================================="
echo "========================================================================================================================"

docker container rm temp --force
docker run --name temp -d codenjoy-source
eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy && git checkout ${REVISION} && git pull origin'" 

echo "========================================================================================================================"
echo "=============================================== Building codenjoy server ==============================================="
echo "========================================================================================================================"

if [ "x$GAME" = "x" ] ; 
then 
    # build all projects
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;
    
    # build war with all games
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/builder && mvn clean install -Dcontext=${CODENJOY_CONTEXT} -DallGames -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;
else
    # build engine
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/games/engine && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;

    # build game
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/games/$GAME_PROJECT && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;

    # build server
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/server && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;

    # build war with selected game
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/builder && mvn clean install -Dcontext=${CODENJOY_CONTEXT} -P$GAME -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;
fi
docker cp temp:/tmp/codenjoy/CodingDojo/builder/target/${CODENJOY_CONTEXT}.war ./${CODENJOY_CONTEXT}.war

echo "========================================================================================================================"
echo "=================================================== Cleanning stuff ===================================================="
echo "========================================================================================================================"

docker commit temp codenjoy-source
docker container rm temp --force


echo "========================================================================================================================"
echo "========================================================= DONE ========================================================="
echo "========================================================================================================================"