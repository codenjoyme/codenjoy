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
    if "%INSTALL_LOCALLY%"=="true" ( set GOPATH=)

    if "%LANG_HOME%"==""    ( set NO_LANG=true)
    if "%NO_LANG%"=="true"  ( set LANG_HOME=%ROOT%\.lang)
    if "%NO_LANG%"=="true"  ( set PATH=%LANG_HOME%\bin;%PATH%)

    set LANG=%LANG_HOME%\bin\go

    echo Language environment variables
    call run :color ‘%CL_INFO%‘ ‘PATH=%PATH%‘
    call run :color ‘%CL_INFO%‘ ‘LANG_HOME=%LANG_HOME%‘

    set ARCH_URL=https://lang.org/lang-1.0.0.zip
    set ARCH_FOLDER=lang-1.0.0
    goto :eof

:install
    call run :install lang %ARCH_URL% %ARCH_FOLDER%
    goto :eof

:version
    call run :eval_echo_color ‘%LANG% version‘
    goto :eof

:build
    call run :eval_echo ‘%LANG% build‘
    goto :eof

:test    
    call run :eval_echo ‘%LANG% test‘
    goto :eof

:run
    rem Please use ‘for external quotes and “for internal“‘ 
    rem We will replace them all with "classic quotes" 
    call run :eval_echo ‘%LANG% run main.lang “%GAME_TO_RUN%“ “%SERVER_URL%“‘
    goto :eof