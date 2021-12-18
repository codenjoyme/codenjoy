@echo off

call run :init_colors

:check_run_mode
    if "%*"=="" (       
        call :run_executable 
    ) else (
        call :run_library %*
    )
    goto :eof

:run_executable
    rem run stuff.bat as executable script
    call run :color ‘%CL_INFO%‘ ‘This is not executable script. Please use 'run.bat' only.‘
    call run :ask   
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

    echo Language environment variables
    call run :color ‘%CL_INFO%‘ ‘PATH=%PATH%‘
    call run :color ‘%CL_INFO%‘ ‘JAVA_HOME=%JAVA_HOME%‘
    call run :color ‘%CL_INFO%‘ ‘MAVEN_HOME=%MAVEN_HOME%‘
    call run :color ‘%CL_INFO%‘ ‘MAVEN_OPTS=%MAVEN_OPTS%‘
    call run :color ‘%CL_INFO%‘ ‘MAVEN_USER_HOME=%MAVEN_USER_HOME%‘
    call run :color ‘%CL_INFO%‘ ‘MVNW_VERBOSE=%MVNW_VERBOSE%‘

    set ARCH_URL=https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip
    set ARCH_FOLDER=jdk-11.0.11+9
    goto :eof

:install
    call run :install jdk %ARCH_URL% %ARCH_FOLDER%
    goto :eof

:version
    call run :eval_echo_color ‘%MVNW% -v‘
    goto :eof

:build
    rem install jar to maven local repo
    call run :eval_echo ‘%MVNW% clean install -DskipTests=%SKIP_TESTS%‘

    rem create executable jar
    call run :eval_echo ‘%MVNW% compile assembly:single -Pjar-with-dependencies -DskipTests=%SKIP_TESTS%‘

    call run :eval_echo ‘copy %ROOT%\target\client-exec.jar %ROOT%\‘
    call run :color ‘%CL_INFO%‘ ‘The executable file is located here: %ROOT%\client-exec.jar‘    
    goto :eof

:test    
    call run :eval_echo ‘%MVNW% clean test‘
    goto :eof

:run   
    rem run jar
    rem call run :eval_echo ‘%JAVA% -Dfile.encoding=UTF-8 -jar %ROOT%\client-exec.jar %BUILD_ARGS%‘

    rem build & run (without jar)
    call run :eval_echo ‘%MVNW% clean compile exec:java -Dfile.encoding=UTF-8 -Dexec.mainClass=com.codenjoy.dojo.%LANGUAGE%Runner -Dexec.args=“%BUILD_ARGS%“‘
    goto :eof