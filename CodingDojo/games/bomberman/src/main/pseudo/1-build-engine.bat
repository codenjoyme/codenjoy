call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+        
echo        !                 Now we are building your engine...                      !        
echo        +-------------------------------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" ( 
    pause >nul
)

cd %ENGINE_HOME%
call mvnw clean install -DskipTests=%SKIP_TESTS%
cd ..

echo Press any key to exit
pause >nul

cd %ROOT%