call .\settings\setup.bat

echo off
echo     +--------------------------------------------+
echo     !     Now we are try to start the server     !
echo     ! Also we open register page on your browser !
echo     !  So you can use it for registration after  !
echo     +--------------------------------------------+
echo on
pause >nul

call explorer http://127.0.0.1:8080/codenjoy-contest

cd %JETTY_HOME%
%JAVA_HOME%\bin\java -jar start.jar

echo Press any key to exit
pause >nul