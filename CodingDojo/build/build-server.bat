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

cd ..

set ROOT=%CD%

IF "%GAMES_TO_RUN%"=="" (
	rem set GAMES_TO_RUN=all
	set GAMES_TO_RUN=mollymage
	rem set GAMES_TO_RUN=tetris,knibert,mollymage
)

echo Building server with [%GAMES_TO_RUN%]

IF "%GAMES_TO_RUN%"=="all" (
    call %ROOT%\mvnw clean install -DskipTests
    
    cd %ROOT%\server
    call %ROOT%\mvnw clean package -DskipTests -DallGames
) else (
    cd %ROOT%\games
    call %ROOT%\mvnw clean install -N

    cd %ROOT%\clients\java
    call %ROOT%\mvnw clean install -DskipTests

    cd %ROOT%\games\engine
    call %ROOT%\mvnw clean install -DskipTests
    
    for %%a in ("%GAMES_TO_RUN:,=" "%") do (
        cd %ROOT%\games\%%~a
        call %ROOT%\mvnw clean install -DskipTests
    )
    
    cd %ROOT%\server
    call %ROOT%\mvnw clean package -DskipTests -P%GAMES_TO_RUN%
)

cd %ROOT%

pause >nul