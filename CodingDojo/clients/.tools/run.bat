@echo off

:check_run_mode
    if "%*"=="" (       
        call :run_executable 
    ) else (
        call :run_library %*
    )
    goto :eof

:run_executable
    rem run run.bat as executable script
    goto :init

:run_library
    rem run run.bat as library
    set input=%* 
    rem replace all ‘ with "
    call set all=%%input:‘="%%
    call %all%
    goto :eof      

:init
    call :init_colors    
    call :settings    
    set OPTION=%1

:start
    if "%OPTION%"=="" (
        call :ask_option
    )
    call :%OPTION%
    goto :restart

:restart
    set OPTION=
    SET BUILD_ARGS=
    goto :start

:init_colors 
    rem red *;91
    rem green *;92
    rem yellow *;93
    rem blue *;94
    rem pink *;95
    rem light blue *;96
    rem purple *;97
    rem black background 40;*
    rem dark yellow background 43;*
    rem purple background 45;*
    rem blue background 44;*
    set CL_HEADER=43;93
    set CL_COMMAND=40;96
    set CL_QUESTION=45;97
    set CL_INFO=44;93
    goto :eof  

:eval_echo
    set input=%~1%
    rem replace all “ with "
    call set command=%%input:“="%%
    call :color "%CL_COMMAND%" "%input%"
    call %command%
    goto :eof

:ask_option
    call :color "%CL_QUESTION%" "What would you like to do: [d]ownload env, [b]uild, [t]est, [r]un or [q]uit?"
    set /p CODE=
    if "%CODE%"=="d" set OPTION=download
    if "%CODE%"=="b" set OPTION=build
    if "%CODE%"=="t" set OPTION=test
    if "%CODE%"=="r" set OPTION=run
    if "%CODE%"=="q" exit
    goto :eof

:read_env
    echo Reading enviromnent from .env file
    for /f "tokens=*" %%i in ('type .env') do call :process_env "%%i"
    set BUILD_ARGS=%BUILD_ARGS:~1%
    goto :eof
    
:process_env
    set line=%~1%
    set %line%
    for /f "tokens=1* delims==" %%a in ("%line%") do set v=%%b
    set BUILD_ARGS=%BUILD_ARGS% %v%
    call :color "%CL_INFO%" "%line%"
    goto :eof

:print_color
	call :color "%CL_COMMAND%" "%*"
	call %* > %TOOLS%\out
	for /f "tokens=*" %%s in (%TOOLS%\out) do (
        call :color "%CL_INFO%" "%%s"
    )
    del /Q %TOOLS%\out
    goto :eof

:color
    set color=%~1%
    set message=%~2%
    echo [%color%m%message%[0m
    echo.
    goto :eof

:ask
    call :color "%CL_QUESTION%" "Press any key to continue"
    pause >nul
    goto :eof

:sep
    call :color "%CL_COMMAND%" "---------------------------------------------------------------------------------------"
    goto :eof

:download_file
    set url=%~1%
    set file=%~2%
    call :color "%CL_COMMAND%" "Downloading '%url%'"
    call :color "%CL_COMMAND%" "       into '%file%'"
    powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; $client=New-Object System.Net.WebClient; $client.Headers['User-Agent'] = 'User-Agent'; $client.Headers.Add([System.Net.HttpRequestHeader]::Cookie, 'oraclelicense=accept-securebackup-cookie'); $client.DownloadFile('%url%','%file%') }"
    goto :eof

:install
    call :eval_echo "cd %ROOT%"
    call :eval_echo "set DEST=%~1"
    call :eval_echo "set URL=%~2"
    call :eval_echo "set FOLDER=%~3"
    IF EXIST %TOOLS%\%DEST%.zip (
        call :eval_echo "del /Q %TOOLS%\%DEST%.zip"
    )
    call :download_file "%URL%" "%TOOLS%\%DEST%.zip"
    call :eval_echo "rd /S /Q %TOOLS%\..\.%DEST%"
    if "%FOLDER%"=="" (
        call :eval_echo "%ARCH% x -y -o%TOOLS%\..\.%DEST% %TOOLS%\%DEST%.zip"
    ) ELSE (
        call :eval_echo "%ARCH% x -y -o%TOOLS%\.. %TOOLS%\%DEST%.zip"
        call :eval_echo "timeout 2"
        call :eval_echo "rename %TOOLS%\..\%FOLDER% .%DEST%"
    )
    call :eval_echo "cd %ROOT%"
    goto :eof

:settings
    call :color "%CL_HEADER%" "Setup variables..."
    call :read_env
    set ROOT=%CD%
    if "%SKIP_TESTS%"==""  ( set SKIP_TESTS=true)
    set CODE_PAGE=65001
    chcp %CODE_PAGE%
    set TOOLS=%ROOT%\.tools
    set ARCH=%TOOLS%\7z\7za.exe
    rem Set to true if you want to ignore platform installation on the system
    if "%INSTALL_LOCALLY%"==""     ( set INSTALL_LOCALLY=true)
    call stuff :settings
    goto :eof

:download
    call :color "%CL_HEADER%" "Installing..."
    if     "%INSTALL_LOCALLY%"=="true" call stuff :install
    if NOT "%INSTALL_LOCALLY%"=="true" call :color "%CL_INFO%" "The environment installed on the system is used"
    call stuff :version
    goto :eof

:build
    call :color "%CL_HEADER%" "Building client..."
    call stuff :version
    call stuff :build
    goto :eof

:test
    call :color "%CL_HEADER%" "Starting tests..."
    call stuff :test
    goto :eof

:run
    call :color "%CL_HEADER%" "Running client..."
    call stuff :run
    goto :eof