call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+        
echo        !                 Now we are starting your client...                      !        
echo        +-------------------------------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" ( 
    pause >nul
)

chcp %CODE_PAGE%
cls

cd %APP_HOME%
call mvnw exec:java -Dfile.encoding=UTF-8 -D"exec.mainClass"="com.codenjoy.dojo.%GAME_TO_RUN%.client.simple.YourSolverLite" -D"exec.args"="%BOARD_URL% %RULES_HOME%"

echo Press any key to exit
pause >nul

cd %ROOT%