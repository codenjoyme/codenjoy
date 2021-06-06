call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+
echo        !                   Now we are building clients...                        !
echo        +-------------------------------------------------------------------------+
echo [0m
echo on

cd %LANGUAGE%
call 2-run.bat
cd %ROOT%

call :ask

goto :eof

:ask
    echo Press any key to continue
    pause >nul
goto :eof