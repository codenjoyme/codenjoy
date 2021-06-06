call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+
echo        !                   Now we are building clients...                        !
echo        +-------------------------------------------------------------------------+
echo [0m
echo on
IF "%DEBUG%"=="true" (
    pause >nul
)

cd %LANGUAGE%
call 2-run.bat
cd %ROOT%

echo Press any key to exit
pause >nul