#!/usr/bin/env bash

if [ "x$CODENJOY_VERSION" = "x" ]; then
    CODENJOY_VERSION=1.0.28
fi

mvn install:install-file -Dfile=engine-$CODENJOY_VERSION.jar -Dsources=engine-$CODENJOY_VERSION-sources.jar -DpomFile=engine-$CODENJOY_VERSION-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=$CODENJOY_VERSION -Dpackaging=jar

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

mvn install:install-file -Dfile=games-$CODENJOY_VERSION-pom.xml -DpomFile=games-$CODENJOY_VERSION-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=$CODENJOY_VERSION -Dpackaging=pom
echo "[93m"
echo "       +--------------------------------------------------+"
echo "       !         Check that BUILD was SUCCESS             !"
echo "       !           Then press Enter to exit               !"
echo "       +--------------------------------------------------+"
echo "[0m"
if [ "x$DEBUG" = "xtrue" ]; then
    read
fi