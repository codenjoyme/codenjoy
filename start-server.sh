#!/bin/bash
mvn install -DskipTests

cd tetris-core 
mvn install 
cd .. 
cd tetris-web 
./startServer.sh