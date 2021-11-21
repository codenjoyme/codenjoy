#!/usr/bin/env bash

install_all() {
   from=$1
   cp $from ../cpp/
   cp $from ../csharp/
   cp $from ../go/
   cp $from ../java/
   cp $from ../java-script/
   cp $from ../kotlin/
   cp $from ../php/
   cp $from ../pseudo/
   cp $from ../python/
   cp $from ../ruby/
   cp $from ../scala/   
}

install_jvm() {
   from=$1
   to=$2
   cp $from ../java/$to
   cp $from ../kotlin/$to
   cp $from ../pseudo/$to
   cp $from ../scala/$to   
}

install_all "run.bat"
install_all "run.sh"
install_all ".env"
install_jvm "jvm_stuff.bat" "stuff.bat"