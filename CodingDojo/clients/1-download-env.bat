@echo off
call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+
echo        !                   Now we are downloading stuff...                       !
echo        +-------------------------------------------------------------------------+
echo [0m
echo on

cd %LANGUAGE%
call 1-download-env.bat
cd %ROOT%

call :ask

:ask
    echo Press any key to continue
    pause >nul
goto :eof