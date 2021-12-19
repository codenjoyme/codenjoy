cd ..

set ROOT=%CD%

IF "%GAMES_TO_RUN%"=="" (
	rem set GAMES_TO_RUN=all
	set GAMES_TO_RUN=mollymage
	rem set GAMES_TO_RUN=tetris,snake,mollymage
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