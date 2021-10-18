if "%GAME_TO_RUN%"=="" ( set GAME_TO_RUN=mollymage)
if "%BOARD_URL%"==""   ( set BOARD_URL=http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000)
if "%LANGUAGE%"==""    ( set LANGUAGE=pseudo)

set CLIENTS_ROOT=%CD%

if "%SKIP_TESTS%"=="" ( set SKIP_TESTS=true)

set CODE_PAGE=65001
chcp %CODE_PAGE%

echo off
echo        [44;93mGAME_TO_RUN=%GAME_TO_RUN%[0m
echo        [44;93mBOARD_URL=%BOARD_URL%[0m
echo        [44;93mLANGUAGE=%LANGUAGE%[0m
echo on

rem Set to true if you want to ignore jdk and maven installed on the system
if "%INSTALL_LOCALLY%"=="" ( set INSTALL_LOCALLY=true)

rem if "%INSTALL_LOCALLY%"=="true" ( set TOOLS=%ROOT%\.tools)
rem if "%INSTALL_LOCALLY%"=="true" ( set ARCH=%TOOLS%\7z\7za.exe)
rem if "%INSTALL_LOCALLY%"=="true" ( set NODE_HOME=%ROOT%\.node)
rem if "%INSTALL_LOCALLY%"=="true" ( set NODE=%NODE_HOME%\node)
rem if "%INSTALL_LOCALLY%"=="true" ( set NPM=%NODE_HOME%\npm)
rem if "%INSTALL_LOCALLY%"=="true" ( set GOPATH=%ROOT%\.golang\go)
rem if "%INSTALL_LOCALLY%"=="true" ( set GO=%GOPATH%\bin\go)
rem if "%INSTALL_LOCALLY%"=="true" ( set GIT_HOME=%ROOT%\.gitTool)
rem if "%INSTALL_LOCALLY%"=="true" ( set GIT=%GIT_HOME%\cmd\git)

rem if "%JAVA_CLIENT_HOME%"==""       ( set JAVA_CLIENT_HOME=%ROOT%\java)
rem if "%PSEUDO_CLIENT_HOME%"==""     ( set PSEUDO_CLIENT_HOME=%ROOT%\pseudo)
rem if "%PSEUDO_RULES%"==""           ( set PSEUDO_RULES=%PSEUDO_CLIENT_HOME%\rules)
rem if "%PSEUDO_HERO_ELEMENTS%"==""   ( set PSEUDO_HERO_ELEMENTS=HERO,POTION_HERO,DEAD_HERO)
rem if "%JAVASCRIPT_CLIENT_HOME%"=="" ( set JAVASCRIPT_CLIENT_HOME=%ROOT%\java-script)
rem if "%GO_CLIENT_HOME%"==""         ( set GO_CLIENT_HOME=%ROOT%\go)
rem if "%PHP_CLIENT_HOME%"==""        ( set PHP_CLIENT_HOME=%ROOT%\php)
rem if "%PHP_HOME%"==""               ( set PHP_HOME=%ROOT%\.php)
rem if "%PYTHON_CLIENT_HOME%"==""     ( set PYTHON_CLIENT_HOME=%ROOT%\python)
rem if "%PYTHON_HOME%"==""            ( set PYTHON_HOME=%ROOT%\.python)

rem if "%ARCH_GIT%"==""          ( set ARCH_GIT=https://github.com/git-for-windows/git/releases/download/v2.18.0.windows.1/MinGit-2.18.0-64-bit.zip)
rem if "%ARCH_NODE%"==""         ( set ARCH_NODE=https://nodejs.org/dist/v14.17.0/node-v14.17.0-win-x64.zip)
rem if "%ARCH_NODE_FOLDER%"==""  ( set ARCH_NODE_FOLDER=node-v14.17.0-win-x64)
rem if "%ARCH_GO%"==""           ( set ARCH_GO=https://golang.org/dl/go1.16.5.windows-amd64.zip)
rem if "%ARCH_GO_FOLDER%"==""    ( set ARCH_GO_FOLDER=go)
rem if "%ARCH_PHP%"==""          ( set ARCH_PHP=https://windows.php.net/downloads/releases/archives/php-8.0.8-nts-Win32-vs16-x64.zip)
rem if "%ARCH_PHP_COMPOSER%"=="" ( set ARCH_PHP_COMPOSER=https://getcomposer.org/download/latest-2.x/composer.phar)
rem if "%ARCH_PYTHON%"==""       ( set ARCH_PYTHON=https://www.python.org/ftp/python/3.9.6/python-3.9.6-embed-amd64.zip)