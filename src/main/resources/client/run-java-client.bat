rem call .\settings\setup.bat

cd .\libs
call setup.bat
cd ..

call %M2_HOME%\bin\mvn clean assembly:assembly

rmdir /S /Q .\out\*.*
md .\out
copy .\target\*.jar .\out\*.*
rem call explorer .\out

echo     +-----------------------------------------------------------+
echo     ! Please register at http://127.0.0.1:8080/codenjoy-contest !
echo     !     For demo you should register user your@email.com      !
echo     !             and also 3 another dummy users                !
echo     ! After that please go back (here) and select your language !
echo     !  Press any key to open registration page in your browser  !
echo     ! (Magic will happen only if the server is already running) !
echo     +-----------------------------------------------------------+
pause >nul
call explorer http://127.0.0.1:8080/codenjoy-contest

cd out
echo     +---------------------------------------------+
echo     ! Now please select your programming language !
echo     +---------------------------------------------+
set /p clientlanguage="[java|kotlin]?"
echo %clientlanguage%
call %JAVA_HOME%\bin\java -jar expansion-engine.jar %clientlanguage%

echo Press any key to exit
pause >nul