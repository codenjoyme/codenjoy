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
    if "%INSTALL_LOCALLY%"=="true" ( set LANG_HOME=)

    if "%LANG_HOME%"==""    ( set NO_LANG=true)
    if "%NO_LANG%"=="true"  ( set LANG_HOME=%ROOT%\.lang)
    if "%NO_LANG%"=="true"  ( set PATH=%LANG_HOME%\bin;%PATH%)

    set LANG=%LANG_HOME%\bin\go

    echo Language environment variables
    call %RUN% :color ‘%CL_INFO%‘ ‘PATH=%PATH%‘
    call %RUN% :color ‘%CL_INFO%‘ ‘LANG_HOME=%LANG_HOME%‘

    set ARCH_URL=https://lang.org/lang-1.0.0.zip
    set ARCH_FOLDER=lang-1.0.0
    goto :eof

:install
    call %RUN% :install lang %ARCH_URL% %ARCH_FOLDER%
    goto :eof

:version
    call %RUN% :eval_echo_color ‘%LANG% version‘
    goto :eof

:build
    call %RUN% :eval_echo ‘%LANG% build‘
    goto :eof

:test
    call %RUN% :eval_echo ‘%LANG% test‘
    goto :eof

:run
    rem Please use ‘for external quotes and “for internal“‘
    rem We will replace them all with "classic quotes"
    call %RUN% :eval_echo ‘%LANG% run main.lang “%GAME_TO_RUN%“ “%SERVER_URL%“‘
    goto :eof