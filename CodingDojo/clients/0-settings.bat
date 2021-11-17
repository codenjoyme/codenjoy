if "%GAME_TO_RUN%"=="" ( set GAME_TO_RUN=mollymage)
if "%BOARD_URL%"==""   ( set BOARD_URL=http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000)
if "%LANGUAGE%"==""    ( set LANGUAGE=java)

rem Set to true if you want to ignore dev-tools installed on the System
if "%INSTALL_LOCALLY%"=="" ( set INSTALL_LOCALLY=true)

rem For pseudo client only
if "%PSEUDO_RULES%"==""    ( set PSEUDO_RULES=%CD%\pseudo\rules)

set CLIENTS_ROOT=%CD%

if "%SKIP_TESTS%"=="" ( set SKIP_TESTS=true)

set CODE_PAGE=65001
chcp %CODE_PAGE%

echo off
echo        [44;93mGAME_TO_RUN=%GAME_TO_RUN%[0m
echo        [44;93mBOARD_URL=%BOARD_URL%[0m
echo        [44;93mLANGUAGE=%LANGUAGE%[0m
echo on
