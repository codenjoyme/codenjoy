#!/bin/bash
mvn install -DskipTests
cd tetris-web 
./startServer.sh