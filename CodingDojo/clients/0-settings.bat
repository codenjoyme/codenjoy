if "%GAME_TO_RUN%"=="" ( set GAME_TO_RUN=mollymage)
if "%BOARD_URL%"==""   ( set BOARD_URL=http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000)
if "%LANGUAGE%"==""    ( set LANGUAGE=java)

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

rem if "%INSTALL_LOCALLY%"=="true" ( set GIT_HOME=%ROOT%\.gitTool)
rem if "%INSTALL_LOCALLY%"=="true" ( set GIT=%GIT_HOME%\cmd\git)

rem if "%PHP_CLIENT_HOME%"==""        ( set PHP_CLIENT_HOME=%ROOT%\php)
rem if "%PHP_HOME%"==""               ( set PHP_HOME=%ROOT%\.php)

rem if "%ARCH_GIT%"==""          ( set ARCH_GIT=https://github.com/git-for-windows/git/releases/download/v2.18.0.windows.1/MinGit-2.18.0-64-bit.zip)
rem if "%ARCH_PHP%"==""          ( set ARCH_PHP=https://windows.php.net/downloads/releases/archives/php-8.0.8-nts-Win32-vs16-x64.zip)
rem if "%ARCH_PHP_COMPOSER%"=="" ( set ARCH_PHP_COMPOSER=https://getcomposer.org/download/latest-2.x/composer.phar)
rem if "%ARCH_PYTHON%"==""       ( )