#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

color() {
    message=$1
    echo "[93m$message[0m"
}

check_machine() {
    unameOut="$(uname -s)"
    case "${unameOut}" in
        Linux*)     machine=Linux;;
        Darwin*)    machine=Mac;;
        CYGWIN*)    machine=Cygwin;;
        MINGW*)     machine=MinGw;;
        *)          machine="UNKNOWN:${unameOut}"
    esac
    echo ${machine}
}

parameter_prefix() {
    machine=$(check_machine)
    if [[ "$machine" == "Mac" || "$machine" == "Linux" ]]; then
        echo --
    else
        echo -D
    fi
}

create_env_file() {
    cat << EOF > $OUT/.env
GAME=$GAME
HOST=127.0.0.1
PORT=8080
TIMEOUT=1000
LOG_DISABLE=false
LOG_FILE=output.txt
LOG_TIME=true
SHOW_PLAYERS=1,2
RANDOM_SEED=random-soul-string
WAIT_FOR=2
SETTINGS={'POTIONS_COUNT':11,'POTION_POWER':7}
EOF
}

create_run_script() {
    param=$(parameter_prefix)
    cat << EOF > $OUT/run-local-server.sh
#!/usr/bin/env bash

eval_echo() {
    command=\$1
    color=94 # blue
    echo "[\${color}m\$command[0m"
    echo
    eval \$command
}

color() {
    message=\$1
    echo "[93m\$message[0m"
}

read_env() {
    for entry in \$(cat \$ROOT/.env); do
        if [[ ! \$entry == \#* ]]; then
            export \$entry
            color \$entry
        fi
    done
}

eval_echo "ROOT=\$(pwd)"
eval_echo "read_env"
eval_echo "java -jar ${param}host=\$HOST ${param}port=\$PORT ${param}timeout=\$TIMEOUT ${param}logDisable=\$LOG_DISABLE ${param}log=\"\$LOG_FILE\" ${param}logTime=\$LOG_TIME ${param}showPlayers=\"\$SHOW_PLAYERS\" ${param}random=\"\$RANDOM_SEED\" ${param}waitFor=\$WAIT_FOR ${param}settings=\"\$SETTINGS\" $GAME-engine.jar"

echo Press Enter to continue
read
EOF
}

echo Please enter game name
read GAME

eval_echo "ROOT=$(pwd)/.."
eval_echo "MVNW=$ROOT/mvnw"
eval_echo "GAME_ROOT=$ROOT/games/$GAME"
eval_echo "ENGINE_ROOT=$ROOT/games/engine"
eval_echo "JAVA_CLIENT_ROOT=$ROOT/clients/java"
eval_echo "OUT=$(pwd)/out"

if [[ ! -d "$GAME_ROOT" ]]; then
    color "Game $GAME does not exists!"
else
    # eval_echo "$MVNW -f $JAVA_CLIENT_ROOT/pom.xml clean install -DskipTests=true"
    # eval_echo "$MVNW -f $ENGINE_ROOT/pom.xml clean install -DskipTests=true"
    # eval_echo "$MVNW -f $GAME_ROOT/pom.xml clean compile assembly:single -DskipTests=true -DgitDir=$ROOT -Pjar-local"
    eval_echo "mkdir $OUT"
    eval_echo "SOURCE=$GAME_ROOT/target/$GAME-engine.jar"
    eval_echo "DEST=$OUT/$GAME-engine.jar"
    eval_echo "cp $SOURCE $DEST"
    eval_echo "create_env_file"
    eval_echo "create_run_script"
    color "Please copy generated stuff from here: $OUT"
    color "Update '.env' file then run 'run-local-server.sh'"
fi

echo Press Enter to continue
read
}