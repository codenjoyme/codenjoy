call 00-settings.bat

del /Q .\files\*.*

del /Q %JETTY_HOME%\database\*.*
del /Q %JETTY_HOME%\gameData\*.*
del /Q %JETTY_HOME%\webapps\%CONTEXT%.war

rd /S /Q %ROOT%\codenjoy\CodingDojo\builder\target

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
    call %GIT_HOME%\cmd\git clone https://github.com/codenjoyme/codenjoy.git
)

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !      Updating Codenjoy from git     !        
echo        +-------------------------------------+        
echo [0m
echo on

cd %ROOT%\codenjoy\CodingDojo
call %GIT_HOME%\cmd\git pull origin master

if NOT "%GIT_REVISION%"=="" (
	call %GIT_HOME%\cmd\git stash --all
	call %GIT_HOME%\cmd\git checkout "%GIT_REVISION%"
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

call %M2_HOME%\bin\mvn clean install -DskipTests=%SKIP_TESTS%

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

cd %ROOT%\codenjoy\CodingDojo\builder

echo off
echo [44;93m
set /p GAMES_TO_RUN="Please select games from list with comma separated (just click Enter to select all games):"
echo [0m
IF "%GAMES_TO_RUN%"=="" (
    call %M2_HOME%\bin\mvn clean package -Dcontext=${CONTEXT} -DallGames
) else (
    call %M2_HOME%\bin\mvn clean package -Dcontext=${CONTEXT} -P%GAMES_TO_RUN%
)

echo off
echo [44;93m
echo        +--------------------------------------------+        
echo        !    Please check that BUILD was [102;30mSUCCESS[44;93m     !        
echo        !      Press any key to deploy on Jetty      !        
echo        +--------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" ( 
    pause >nul 
)

cd %ROOT%

rd /S /Q %ROOT%\files
mkdir %ROOT%\files
rd /S /Q %ROOT%\files\engine-libs
mkdir %ROOT%\files\engine-libs

copy %ROOT%\codenjoy\CodingDojo\games\engine\setup.bat %ROOT%\files\engine-libs\*.*
copy %M2_HOME%\.m2\repository\com\codenjoy\engine\%CODENJOY_VERSION%\engine-%CODENJOY_VERSION%.jar %ROOT%\files\engine-libs\*.*
copy %M2_HOME%\.m2\repository\com\codenjoy\engine\%CODENJOY_VERSION%\engine-%CODENJOY_VERSION%.pom %ROOT%\files\engine-libs\engine-%CODENJOY_VERSION%-pom.xml
copy %M2_HOME%\.m2\repository\com\codenjoy\engine\%CODENJOY_VERSION%\engine-%CODENJOY_VERSION%-sources.jar %ROOT%\files\engine-libs\*.*
copy %M2_HOME%\.m2\repository\com\codenjoy\games\%CODENJOY_VERSION%\games-%CODENJOY_VERSION%.pom %ROOT%\files\engine-libs\games-%CODENJOY_VERSION%-pom.xml

cd %ROOT%\files\engine-libs
call setup.bat
cd %ROOT%

copy %ROOT%\codenjoy\CodingDojo\builder\target\%CONTEXT%.war %ROOT%\files\*.*
copy %ROOT%\files\%CONTEXT%.war %JETTY_HOME%\webapps\*.*

cd %ROOT%

echo off
echo [44;93m
echo        +--------------------------------------------+        
echo        !     Now you can run [102;30m2-start-server.bat[44;93m     !        
echo        +--------------------------------------------+        
echo [0m
echo on
pause >nul