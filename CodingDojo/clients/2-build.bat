call 0-settings.bat

echo off
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo        [44;93m!                   Now we are building clients...                        ![0m
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo on

set BUILD_LANGUAGE=%LANGUAGE%

IF "%LANGUAGE%"=="pseudo" (
    set BUILD_LANGUAGE=java
    call :build

    set BUILD_LANGUAGE=pseudo
    call :build
)

IF "%LANGUAGE%"=="php" (
    call :php
)

IF "%LANGUAGE%"=="python" (
    call :python
)

call :build
goto :eof

:build
    cd %BUILD_LANGUAGE%
    call 2-build.bat
    cd %ROOT%
goto :eof

:php
    SET PATH=%PHP_HOME%;%PATH%
goto :eof

:python
    SET PATH=%PYTHON_HOME%;%PATH%
goto :eof