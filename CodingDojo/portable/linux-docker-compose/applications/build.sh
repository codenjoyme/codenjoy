if [ "x$BUILD_SERVER" = "x" ] ; 
then
	BUILD_SERVER=true
fi

if [ "x$BUILD_BALANCER" = "x" ] ; 
then
	BUILD_BALANCER=true
fi

if [ "x$GIT_REPO" = "x" ] ; 
then
	GIT_REPO=https://github.com/codenjoyme/codenjoy.git
fi

if [ "x$REVISION" = "x" ] ; 
then
	REVISION=master
fi

if [ "x$CODENJOY_CONTEXT" = "x" ] ; 
then
	CODENJOY_CONTEXT=codenjoy-contest
fi

if [ "x$GAME" = "x" ] ; 
then
	GAME=snakebattle
    # this difference is only for snake-battle
    GAME_PROJECT=snake-battle 
fi

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

    # checkout and build project
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

if [ "x$BUILD_SERVER" = "x" ] ; 
then
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
fi
    
echo "========================================================================================================================"
echo "=============================================== Building balancer server ==============================================="
echo "========================================================================================================================"

if [ "x$BUILD_BALANCER" = "x" ] ; 
then
    # build engine
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/games/engine && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;

    # build balancer
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/balancer && mvn clean install -DskipTests=true' |& tee ./logs/balancer-deploy.log"
    docker cp temp:/tmp/codenjoy/CodingDojo/balancer/target/codenjoy-balancer.war ./codenjoy-balancer.war
fi
    
echo "========================================================================================================================"
echo "=================================================== Cleaning stuff ===================================================="
echo "========================================================================================================================"

docker commit temp codenjoy-source
docker container rm temp --force
