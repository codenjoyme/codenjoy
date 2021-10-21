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