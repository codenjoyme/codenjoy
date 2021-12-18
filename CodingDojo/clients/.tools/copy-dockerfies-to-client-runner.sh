#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

copy_dockerfiles() {
    from_lng=$1
    to_lng=$1
    to_dir=../../client-runner/src/main/resources/dockerfiles
    if [[ "$from_lng" == "java-script" ]]; then
        to_lng=javascript
    fi
    eval_echo "cp ../$from_lng/Dockerfile $to_dir/$to_lng/"
}

eval_echo "copy_dockerfiles 'csharp'"
eval_echo "copy_dockerfiles 'go'"
eval_echo "copy_dockerfiles 'java'"
eval_echo "copy_dockerfiles 'java-script'"
eval_echo "copy_dockerfiles 'kotlin'"
eval_echo "copy_dockerfiles 'php'"
eval_echo "copy_dockerfiles 'pseudo'"
eval_echo "copy_dockerfiles 'python'"
eval_echo "copy_dockerfiles 'ruby'"
eval_echo "copy_dockerfiles 'scala'"

echo Press Enter to continue
read