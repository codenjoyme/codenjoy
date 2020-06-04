set ROOT=%CD%

set GAMES_TO_RUN=tetris,snake,bomberman,sample,snakebattle,sokoban,spacerace,startandjump,sudoku,rubicscube,a2048,battlecity,chess,collapse,expansion,fifteen,football,hex,icancode,kata,lemonade,loderunner,lunolet,minesweeper,moebius,pong,puzzlebox,quadro,quake2d,reversi,excitebike

IF "%GAMES_TO_RUN%"=="" (
    call %ROOT%\mvnw clean install -DskipTests
    
    cd %ROOT%\server
    call %ROOT%\mvnw clean package -DskipTests -DallGames
) else (
    cd %ROOT%\games
    call %ROOT%\mvnw clean install -N
    
    cd %ROOT%\games\engine
    call %ROOT%\mvnw clean install -DskipTests
    
    for %%a in ("%GAMES_TO_RUN:,=" "%") do (
        cd %ROOT%\games\%%~a
        call %ROOT%\mvnw clean install -DskipTests
    )
    
    cd %ROOT%\server
    call %ROOT%\mvnw clean package -DskipTests -P%GAMES_TO_RUN%
)

pause >nul