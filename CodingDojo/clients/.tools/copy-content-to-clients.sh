#!/usr/bin/env bash

eval_echo() {
    command=$1
    color=94 # blue
    echo "[${color}m$command[0m"
    echo
    eval $command
}

install_all() {
   from=content/$1
   eval_echo "cp $from ../cpp/"
   eval_echo "cp $from ../csharp/"
   eval_echo "cp $from ../go/"
   eval_echo "cp $from ../java/"
   eval_echo "cp $from ../java-script/"
   eval_echo "cp $from ../kotlin/"
   eval_echo "cp $from ../php/"
   eval_echo "cp $from ../pseudo/"
   eval_echo "cp $from ../python/"
   eval_echo "cp $from ../ruby/"
   eval_echo "cp $from ../scala/"
}

install_jvm() {
   from=content/$1
   to=$2
   eval_echo "cp $from ../java/$to"
   eval_echo "cp $from ../kotlin/$to"
   eval_echo "cp $from ../pseudo/$to"
   eval_echo "cp $from ../scala/$to"
}

eval_echo "install_all 'run.bat'"
eval_echo "install_all 'run.sh'"
eval_echo "install_all '.env'"
eval_echo "install_jvm 'jvm_stuff.bat' 'stuff.bat'"

echo Press Enter to continue
read