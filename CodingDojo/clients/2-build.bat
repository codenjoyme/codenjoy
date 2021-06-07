call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------------------------------------------+
echo        !                   Now we are building clients...                        !
echo        +-------------------------------------------------------------------------+
echo [0m
echo on

IF "%LANGUAGE%"=="java" (
    call :java
)

IF "%LANGUAGE%"=="pseudo" (
    call :java
    call :pseudo
)

IF "%LANGUAGE%"=="java-script" (
    call :node
)

echo off
echo [44;93m
echo        +-------------------------------------+
echo        !      Now you can run 3-run.bat      !
echo        +-------------------------------------+
echo [0m
echo on

call :ask

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

:node
    cd %JAVASCPIPT_CLIENT_HOME%
    call 1-build.bat
    cd %ROOT%
goto :eof

:ask
    echo Press any key to continue
    pause >nul
goto :eof