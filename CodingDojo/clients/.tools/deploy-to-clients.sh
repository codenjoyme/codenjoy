#!/usr/bin/env bash

install() {
   file=$1
   cp $file ../cpp/
   cp $file ../csharp/
   cp $file ../go/
   cp $file ../java/
   cp $file ../java-script/
   cp $file ../kotlin/
   cp $file ../php/
   cp $file ../pseudo/
   cp $file ../python/
   cp $file ../ruby/
   cp $file ../scala/   
}

install "run.bat"
install "run.sh"