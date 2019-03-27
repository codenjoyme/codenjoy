call 00-settings.bat

del /Q .\files\*.*

rd /S /Q %ROOT%\codenjoy\CodingDojo\server\target

cd %ROOT%

echo off
if not exist %ROOT%\codenjoy (
    echo [44;93m
    echo        +-------------------------------------+        
    echo        !      Clonning Codenjoy from git     !        
    echo        +-------------------------------------+        
    echo [0m
    echo on

    mkdir %ROOT%\codenjoy
    call %GIT_HOME%\cmd\git clone %GIT_REPO%
)

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !      Updating Codenjoy from git     !        
echo        +-------------------------------------+        
echo [0m
echo on

cd %ROOT%\codenjoy\CodingDojo

call %GIT_HOME%\cmd\git config --global core.autocrlf true
if "%GIT_REVISION%"=="" (
	set GIT_REVISION=master
)
if not "%GIT_REVISION%"=="local" (
	call %GIT_HOME%\cmd\git clean -fx
	call %GIT_HOME%\cmd\git reset --hard
	call %GIT_HOME%\cmd\git fetch --all
	call %GIT_HOME%\cmd\git checkout "%GIT_REVISION%"
	call %GIT_HOME%\cmd\git pull origin
	call %GIT_HOME%\cmd\git status"
)

echo off
echo [44;93m
echo        +-------------------------------------------------------------+        
echo        !    Please check that poject update from git was successful  !        
echo        !  There must be a message [102;30mAlready up-to-date[44;93m without errors  !        
echo        !            After that we are try build server               !        
echo        !                 Press any key to continue                   !        
echo        +-------------------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" ( 
    pause >nul 
)

call mvnw clean install -DskipTests=%SKIP_TESTS%

echo off
echo [44;93m
echo        +--------------------------------------------+        
echo        !    Please check that BUILD was [102;30mSUCCESS[44;93m     !        
echo        !   Press any key to build games you want    !        
echo        +--------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" (
    pause >nul
)

cd %ROOT%\codenjoy\CodingDojo\server

echo off
echo [44;93m
set /p GAMES_TO_RUN="Please select games from list with comma separated (just click Enter to select all games):"
echo [0m
IF "%GAMES_TO_RUN%"=="" (
    call ..\mvnw clean package -DskipTests -DallGames
) else (
    call ..\mvnw clean package -DskipTests -P%GAMES_TO_RUN%
)

mkdir %APP_HOME%
echo %ROOT%\codenjoy\CodingDojo\server\target\codenjoy-contest.war
copy %ROOT%\codenjoy\CodingDojo\server\target\codenjoy-contest.war %APP_HOME%\server.war

cd %ROOT%

