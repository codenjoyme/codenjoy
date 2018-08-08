call .\settings\setup.bat

del /Q .\files\*.*

del /Q %JETTY_HOME%\database\*.*
del /Q %JETTY_HOME%\gameData\*.*
del /Q %JETTY_HOME%\webapps\codenjoy-contest.war

rd /S /Q %ROOT%\codenjoy-sources\CodingDojo\builder\target

echo off
echo     +-------------------------------------+
echo     !   All build files are cleaned       !
echo     !   Please run 1-rebuild-server.bat   !
echo     +-------------------------------------+
echo on
pause >nul
