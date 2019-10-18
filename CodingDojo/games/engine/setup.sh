#!/usr/bin/env bash

if [ "x$CODENJOY_VERSION" = "x" ]; then
    CODENJOY_VERSION=1.1.1
fi

mkdir .\.mvn
mkdir .\.mvn\wrapper

MVNW=$(pwd)\mvnw

if [ -f ".\engine-$CODENJOY_VERSION-pom.xml" ]; then
    mkdir .\target
    cp .\pom.xml .\target\engine-$CODENJOY_VERSION-pom.xml
    cp .\games-pom.xml .\target\games-$CODENJOY_VERSION-pom.xml
elif [ ! -d ".\target" ]; then
        $MVNW clean install -DskipTests=true
        cp .\pom.xml .\target\engine-$CODENJOY_VERSION-pom.xml
        cp ..\pom.xml .\target\games-$CODENJOY_VERSION-pom.xml
    fi
fi

cd .\target

$MVNW install:install-file -Dfile=engine-$CODENJOY_VERSION.jar -Dsources=engine-$CODENJOY_VERSION-sources.jar -DpomFile=engine-$CODENJOY_VERSION-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=$CODENJOY_VERSION -Dpackaging=jar

echo "[93m"
echo "       +--------------------------------------------------+"
echo "       !           Check that BUILD was SUCCESS           !"
echo "       !      Then press Enter to continue (and wait      !"
echo "       !   until the next step installation is complete)  !"
echo "       +--------------------------------------------------+"
echo "[0m"
if [ "x$DEBUG" = "xtrue" ]; then
    read
fi

$MVNW install:install-file -Dfile=games-$CODENJOY_VERSION-pom.xml -DpomFile=games-$CODENJOY_VERSION-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=$CODENJOY_VERSION -Dpackaging=pom
echo "[93m"
echo "       +--------------------------------------------------+"
echo "       !         Check that BUILD was SUCCESS             !"
echo "       !           Then press Enter to exit               !"
echo "       +--------------------------------------------------+"
echo "[0m"
if [ "x$DEBUG" = "xtrue" ]; then
    read
fi