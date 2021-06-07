if "%GAME_TO_RUN%"=="" ( set GAME_TO_RUN=bomberman )
if "%BOARD_URL%"==""  ( set BOARD_URL=http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000 )
if "%LANGUAGE%"==""   ( set LANGUAGE=java-script )

if "%ROOT%"==""       ( set ROOT=%CD% )
if "%TOOLS%"==""      ( set TOOLS=%ROOT%\.tools )
if "%JAVA_HOME%"==""  ( set JAVA_HOME=%ROOT%\.jdk )
if "%MAVEN_HOME%"=="" ( set MAVEN_HOME=%ROOT%\.maven )
if "%NODE_HOME%"==""  ( set NODE_HOME=%ROOT%\.node )
if "%GIT%"==""        ( set GIT=%GIT_HOME%\cmd\git )

if "%JAVA_CLIENT_HOME%"==""       ( set JAVA_CLIENT_HOME=%ROOT%\java )

if "%PSEUDO_CLIENT_HOME%"==""     ( set PSEUDO_CLIENT_HOME=%ROOT%\pseudo )
if "%PSEUDO_RULES%"==""           ( set PSEUDO_RULES=%PSEUDO_CLIENT_HOME%\rules )
if "%PSEUDO_HERO_ELEMENTS%"==""   ( set PSEUDO_HERO_ELEMENTS=BOMBERMAN,BOMB_BOMBERMAN,DEAD_BOMBERMAN )

if "%JAVASCRIPT_CLIENT_HOME%"=="" ( set JAVASCRIPT_CLIENT_HOME=%ROOT%\java-script )

if "%ARCH_JDK%"==""         ( set ARCH_JDK=https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip )
if "%ARCH_JDK_FOLDER%"==""  ( set ARCH_JDK_FOLDER=jdk-11.0.11+9 )

if "%ARCH_NODE%"==""        ( set ARCH_NODE=https://nodejs.org/dist/v14.17.0/node-v14.17.0-win-x64.zip )
if "%ARCH_NODE_FOLDER%"=="" ( set ARCH_NODE_FOLDER=node-v14.17.0-win-x64 )

if "%SKIP_TESTS%"=="" ( set SKIP_TESTS=true )
if "%MAVEN_OPTS%"=="" ( set MAVEN_OPTS=-Dmaven.repo.local=%MAVEN_HOME%\repository )
if "%CODE_PAGE%"==""  ( set CODE_PAGE=65001 )