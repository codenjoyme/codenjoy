set GAME_TO_RUN=bomberman
set BOARD_URL=http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000
set LANGUAGE=java-script

set ROOT=%CD%
set TOOLS=%ROOT%\.tools
set JAVA_HOME=%ROOT%\.jdk
set MAVEN_HOME=%ROOT%\.maven
set NODE_HOME=%ROOT%\.node

set JAVA_CLIENT_HOME=%ROOT%\java

set PSEUDO_CLIENT_HOME=%ROOT%\pseudo
set PSEUDO_RULES=%PSEUDO_CLIENT_HOME%\rules
set PSEUDO_HERO_ELEMENTS=BOMBERMAN,BOMB_BOMBERMAN,DEAD_BOMBERMAN

set JAVASCRIPT_CLIENT_HOME=%ROOT%\java-script

set ARCH_JDK=https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip
set ARCH_NODE=https://nodejs.org/dist/v14.17.0/node-v14.17.0-win-x64.zip
set ARCH_NODE_FOLDER=node-v14.17.0-win-x64

set SKIP_TESTS=true
set MAVEN_OPTS="-Dmaven.repo.local=%MAVEN_HOME%\repository"
set CODE_PAGE=65001