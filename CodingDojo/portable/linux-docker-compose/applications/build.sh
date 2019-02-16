#!/usr/bin/env bash
echo "[92m========================================================================================================================"
echo "================================================= Building applications ================================================"
echo "========================================================================================================================[0m"

if [ "x$BUILD_SERVER" = "x" ]; then
    BUILD_SERVER=true
fi

if [ "x$BUILD_BALANCER" = "x" ]; then
    BUILD_BALANCER=true
fi

if [ "x$GIT_REPO" = "x" ]; then
    GIT_REPO=https://github.com/codenjoyme/codenjoy.git
fi

if [ "x$REVISION" = "x" ]; then
    REVISION=master
fi

if [ "x$CODENJOY_CONTEXT" = "x" ]; then
    CODENJOY_CONTEXT=codenjoy-contest
fi

if [ "x$GAME" = "x" ]; then
    GAME=snakebattle
    # this difference is only for snake-battle
    GAME_PROJECT=snake-battle 
fi

echo "[93m"
echo "BUILD_SERVER=$BUILD_SERVER"
echo "BUILD_BALANCER=$BUILD_BALANCER"
echo "GIT_REPO=$GIT_REPO"
echo "REVISION=$REVISION"
echo "GAME=$GAME"
echo "GAME_PROJECT=$GAME_PROJECT"
echo "[0m"

eval_echo() {
    to_run=$1
    echo "[94m"
    echo $to_run
    echo "[0m"

    eval $to_run
}

rm -rf ./logs
mkdir ./logs

# checkout if needed
if [[ "$(docker images -q codenjoy-source 2> /dev/null)" == "" ]]; then
    echo "[92m========================================================================================================================"
    echo "======================================== Codenjoy sources not found. Checking out ======================================="
    echo "========================================================================================================================[0m"

    # remove everything
    eval_echo "docker rmi codenjoy-deploy --force" ;
    eval_echo "docker rmi codenjoy-source --force" ;
    eval_echo "docker rmi java-workspace --force" ;

    # build workspace
    eval_echo "docker build --target jetty_updated -t jetty-updated . |& tee ./logs/java-workspace.log" ;

    # build workspace
    eval_echo "docker build --target java_workspace -t java-workspace . |& tee ./logs/java-workspace.log" ;

    # checkout and build project
    eval_echo "docker build --target codenjoy_source -t codenjoy-source . --build-arg GIT_REPO=${GIT_REPO} --build-arg REVISION=${REVISION} --build-arg SKIP_TESTS=true |& tee ./logs/codenjoy-source.log" ;
fi

echo "[92m========================================================================================================================"
echo "=============================================== Checking out last version =============================================="
echo "========================================================================================================================[0m"

eval_echo "docker container rm temp --force"
eval_echo "docker run --name temp -d codenjoy-source"
eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy && git checkout ${REVISION} && git pull origin'"

echo "[92m========================================================================================================================"
echo "=============================================== Building codenjoy server ==============================================="
echo "========================================================================================================================[0m"

if [ "x$BUILD_SERVER" = "xtrue" ]; then
    if [ "x$GAME" = "x" ]; then 
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
    eval_echo "docker cp temp:/tmp/codenjoy/CodingDojo/builder/target/${CODENJOY_CONTEXT}.war ./${CODENJOY_CONTEXT}.war" ;
fi
    
echo "[92m========================================================================================================================"
echo "=============================================== Building balancer server ==============================================="
echo "========================================================================================================================[0m"

if [ "x$BUILD_BALANCER" = "xtrue" ] ;
then
    # build engine
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/games/engine && mvn clean install -DskipTests=true' |& tee ./logs/codenjoy-deploy.log" ;

    # build balancer
    eval_echo "docker exec temp bash -c 'cd /tmp/codenjoy/CodingDojo/balancer && mvn clean install -DskipTests=true' |& tee ./logs/balancer-deploy.log" ;
    eval_echo "docker cp temp:/tmp/codenjoy/CodingDojo/balancer/target/codenjoy-balancer.war ./codenjoy-balancer.war" ;
fi
    
echo "[92m========================================================================================================================"
echo "=================================================== Cleaning stuff ===================================================="
echo "========================================================================================================================[0m"

eval_echo "docker commit temp codenjoy-source"
eval_echo "docker container rm temp --force"

eval_echo "bash check-revision.sh"