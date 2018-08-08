call .\settings\setup.bat

if not exist %ROOT%\codenjoy (
   mkdir %ROOT%\codenjoy
   call %GIT_HOME%\cmd\git clone https://github.com/codenjoyme/codenjoy.git
)

cd %ROOT%\codenjoy\CodingDojo
call %GIT_HOME%\cmd\git pull origin master

echo off
echo     +-------------------------------------------------------------+
echo     !    Please check that poject updat from git was successful   !
echo     ! There must be a message 'Already up-to-date' without errors !
echo     !                  Press any key to do it                     !
echo     +-------------------------------------------------------------+
echo on
pause >nul

call %M2_HOME%\bin\mvn clean install -DskipTests=%SKIP_TESTS%

echo off
echo     +--------------------------------------------+
echo     !   Please check that BUILD are SUCCESSFUL   !
echo     !    After that we are try build server      !
echo     !         Press any key to do it             !
echo     +--------------------------------------------+
echo on
pause >nul

cd %ROOT%\codenjoy\CodingDojo\builder

echo off
set /p GAMES_TO_RUN="Please select games from list with comma separated (just click Enter for all games):"
IF "%GAMES_TO_RUN%"=="" (
	call %M2_HOME%\bin\mvn clean package -DallGames
) else (
	call %M2_HOME%\bin\mvn clean package -P%GAMES_TO_RUN%
)

echo off
echo     +--------------------------------------------+
echo     !   Please check that BUILD are SUCCESSFUL   !
echo     !         Press any key to continue          !
echo     +--------------------------------------------+
echo on
pause >nul

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

copy %ROOT%\codenjoy\CodingDojo\builder\target\codenjoy-contest.war %ROOT%\files\*.*

copy %ROOT%\files\codenjoy-contest.war %JETTY_HOME%\webapps\*.*

echo off
echo     +--------------------------------------------+
echo     !     Now you can run 2-start-server.bat     !
echo     +--------------------------------------------+
echo on
pause >nul