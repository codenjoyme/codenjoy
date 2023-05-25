@echo off
rem #%L
rem Codenjoy - it's a dojo-like platform from developers to developers.
rem %%
rem Copyright (C) 2012 - 2022 Codenjoy
rem %%
rem This program is free software: you can redistribute it and/or modify
rem it under the terms of the GNU General Public License as
rem published by the Free Software Foundation, either version 3 of the
rem License, or (at your option) any later version.
rem
rem This program is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
rem GNU General Public License for more details.
rem
rem You should have received a copy of the GNU General Public
rem License along with this program.  If not, see
rem <http://www.gnu.org/licenses/gpl-3.0.html>.
rem #L%
@echo on

@echo off

if "%RUN%"=="" set RUN=%CD%\run
if "%STUFF%"=="" set STUFF=%CD%\stuff

call %RUN% :init_colors

:check_run_mode
    if "%*"=="" (
        call :run_executable
    ) else (
        call :run_library %*
    )
    goto :eof

:run_executable
    rem run stuff.bat as executable script
    call %RUN% :color ‘%CL_INFO%‘ ‘This is not executable script. Please use 'run.bat' only.‘
    call %RUN% :ask
    goto :eof

:run_library
    rem run stuff.bat as library
    call %*
    goto :eof

:settings
    if "%INSTALL_LOCALLY%"=="true" ( set JAVA_HOME=)
    if "%INSTALL_LOCALLY%"=="true" ( set MAVEN_HOME=)

    if "%JAVA_HOME%"==""    ( set NO_JAVA=true)
    if "%NO_JAVA%"=="true"  ( set JAVA_HOME=%ROOT%\.jdk)
    if "%NO_JAVA%"=="true"  ( set PATH=%JAVA_HOME%\bin;%PATH%)

    if "%MAVEN_HOME%"==""   ( set NO_MAVEN=true)
    if "%NO_MAVEN%"=="true" ( set MAVEN_HOME=%ROOT%\.mvn)
    if "%NO_MAVEN%"=="true" ( set MAVEN_USER_HOME=%MAVEN_HOME%)
    if "%NO_MAVEN%"=="true" ( set MAVEN_OPTS=-Dmaven.repo.local=%MAVEN_HOME%\repository)

    set MVNW=%ROOT%\mvnw -f %ROOT%\pom.xml
    set MVNW_VERBOSE=false
    set JAVA=%JAVA_HOME%\bin\java

    set RUN_EXECUTABLE_WAR=true

    echo Language environment variables
    call %RUN% :color ‘%CL_INFO%‘ ‘PATH=%PATH%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘JAVA_HOME=%JAVA_HOME%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘MAVEN_HOME=%MAVEN_HOME%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘MAVEN_OPTS=%MAVEN_OPTS%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘MAVEN_USER_HOME=%MAVEN_USER_HOME%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘MVNW_VERBOSE=%MVNW_VERBOSE%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘RUN_EXECUTABLE_WAR=%RUN_EXECUTABLE_WAR%‘

    set ARCH_URL=https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip
    set ARCH_FOLDER=jdk-11.0.11+9
    goto :eof

:install
    call %RUN% :install jdk %ARCH_URL% %ARCH_FOLDER%
    goto :eof

:version
    call %RUN% :eval_echo_color ‘%MVNW% -v‘
    goto :eof

:build
    call %RUN% :color ‘%CL_HEADER%‘ ‘Install jar to maven local repo...‘
    call %RUN% :eval_echo ‘%MVNW% clean install -DskipTests=%SKIP_TESTS%‘

    call %RUN% :color ‘%CL_HEADER%‘ ‘Сreate executable jar...‘
    call %RUN% :eval_echo ‘%MVNW% package assembly:single -Pjar-with-dependencies -DskipTests=%SKIP_TESTS%‘

    call %RUN% :eval_echo ‘copy %ROOT%\target\client-exec.jar %ROOT%\‘
    call %RUN% :color ‘%CL_INFO%‘ ‘The executable file is located here: %ROOT%\client-exec.jar‘
    goto :eof

:test
    call %RUN% :eval_echo ‘%MVNW% clean test‘
    goto :eof

:run
    call %RUN% :color ‘%CL_INFO%‘ ‘RUN_EXECUTABLE_WAR=%RUN_EXECUTABLE_WAR%‘

    if "%RUN_EXECUTABLE_WAR%"=="true" (
        call %RUN% :eval_echo ‘%JAVA% -Dfile.encoding=UTF-8 -jar %ROOT%\client-exec.jar %BUILD_ARGS%‘
    )

    if "%RUN_EXECUTABLE_WAR%"=="false" (
        call %RUN% :eval_echo ‘%MVNW% clean compile exec:java -Dfile.encoding=UTF-8 -Dexec.mainClass=com.codenjoy.dojo.%LANGUAGE%Runner -Dexec.args=“%BUILD_ARGS%“‘
    )
    goto :eof