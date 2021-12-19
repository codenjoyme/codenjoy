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
   to=$2
   eval_echo "cp $from ../cpp/$to"
   eval_echo "cp $from ../csharp/$to"
   eval_echo "cp $from ../go/$to"
   eval_echo "cp $from ../java/$to"
   eval_echo "cp $from ../java-script/$to"
   eval_echo "cp $from ../kotlin/$to"
   eval_echo "cp $from ../php/$to"
   eval_echo "cp $from ../pseudo/$to"
   eval_echo "cp $from ../python/$to"
   eval_echo "cp $from ../ruby/$to"
   eval_echo "cp $from ../scala/$to"
}

install_jvm() {
   from=content/$1
   to=$2
   eval_echo "cp $from ../java/$to"
   eval_echo "cp $from ../kotlin/$to"
   eval_echo "cp $from ../pseudo/$to"
   eval_echo "cp $from ../scala/$to"
}

eval_echo "install_all 'run.bat' 'build/run.bat'"
eval_echo "install_all 'run.sh' 'build/run.sh'"
# eval_echo "install_all '.env' '.env'"
eval_echo "install_jvm 'jvm_stuff.bat' 'build/stuff.bat'"

echo Press Enter to continue
read