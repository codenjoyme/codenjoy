#!/usr/bin/env bash

copy_dockerfiles() {
    from_lng=$1
    to_lng=$1
    to_dir=../../client-runner/src/main/resources/dockerfiles
    if [[ "$from_lng" == "java-script" ]]; then
        to_lng=javascript
    fi
    cp ../$from_lng/Dockerfile $to_dir/$to_lng/
}

copy_dockerfiles "csharp"
copy_dockerfiles "go"
copy_dockerfiles "java"
copy_dockerfiles "java-script"
copy_dockerfiles "kotlin"
copy_dockerfiles "php"
copy_dockerfiles "pseudo"
copy_dockerfiles "python"
copy_dockerfiles "ruby"
copy_dockerfiles "scala"