#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

eval_echo "ssh-agent -s"
eval_echo "ssh-add ~/.ssh/*_rsa"

eval_echo "./mvnw -DaltDeploymentRepository=snapshots::default::file:repo/snapshots clean deploy -DskipTests=true -DgitDir=./../ 2>&1 | tee snapshot-deploy.log" 

eval_echo "cd repo"

eval_echo "mv .git_ .git"
eval_echo "mv .gitignore_ .gitignore"

eval_echo "git add ."
eval_echo "git commit -m'New release'"
eval_echo "git push origin master"

eval_echo "mv .git .git_"
eval_echo "mv .gitignore .gitignore_"

eval_echo "cd .."

echo Press any key to continue
read