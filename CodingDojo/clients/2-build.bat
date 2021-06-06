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
    cd %JAVA_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%
)

IF "%LANGUAGE%"=="pseudo" (
    cd %JAVA_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%

    cd %PSEUDO_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%
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