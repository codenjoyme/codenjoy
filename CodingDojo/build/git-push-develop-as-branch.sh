#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

eval_echo "cd .."

eval_echo "`ssh-agent -s`"
eval_echo "ssh-add ~/.ssh/*_rsa"

echo Please enter branch name
read BRANCH

echo Do we need to commit submudules: y/n?
read submodule_commit
if [[ "$submodule_commit" == "y" ]]; then
	eval_echo "git submodule foreach git push origin master"
fi

eval_echo "git checkout -B $BRANCH"
eval_echo "git push origin $BRANCH"
eval_echo "git checkout develop"
eval_echo "git branch -D $BRANCH"

echo Press Enter to continue
read