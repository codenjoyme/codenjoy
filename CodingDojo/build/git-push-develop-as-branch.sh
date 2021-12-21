#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
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

show_branch_on_git() {
  url=https://github.com/codenjoyme/codenjoy/compare/develop%2E%2E%2E$BRANCH
	machine=$(check_machine)
    if [[ "$machine" == "Mac" || "$machine" == "Linux" ]]; then
        eval_echo "open $url"
    else
        eval_echo "explorer $url"
    fi
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

eval_echo "show_branch_on_git"

echo Press Enter to continue
read