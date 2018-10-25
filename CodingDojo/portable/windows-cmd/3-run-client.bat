call 00-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+        
echo        !                 Now we are starting your client...                      !        
echo        !           Please register your@mail.com user without password           !        
echo        !        Or copy browser URL after registration into YourSolver.java      !  
echo        !      into com.codenjoy.dojo.(SELECTED_GAME).client package from client  !  
echo        +-------------------------------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" ( 
    pause >nul
)

echo off
echo [44;93m
set /p GAME_TO_RUN="Please select game (snake or bomberman for example):"
echo [0m

Set SELECTED_GAME=%ROOT%\client\%GAME_TO_RUN%-servers

if exist %SELECTED_GAME% (
	cd %SELECTED_GAME%
	call %M2_HOME%\bin\mvn clean install -DskipTests=%SKIP_TESTS%
	call %M2_HOME%\bin\mvn exec:java -D"exec.mainClass"="com.codenjoy.dojo.%GAME_TO_RUN%.client.YourSolver"
)

if not exist %SELECTED_GAME% (
	echo off
	echo [44;93m
	echo        +-------------------------------------------------------------------------+        
	echo        !                 There is no client for this game                        !        
	echo        +-------------------------------------------------------------------------+        
	echo [0m
	echo on	
)

echo Press any key to exit
pause >nul

cd %ROOT%