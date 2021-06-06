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

IF "%LANGUAGE%"=="java" (
    call :java
)

IF "%LANGUAGE%"=="pseudo" (
    call :java
    call :pseudo
)

echo off
echo [44;93m
echo        +-------------------------------------+
echo        !      Now you can run 3-run.bat      !
echo        +-------------------------------------+
echo [0m
echo on

echo Press any key to exit
pause >nul

goto :eof

:java
    cd %JAVA_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%
goto :eof

:pseudo
    cd %PSEUDO_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%
goto :eof
