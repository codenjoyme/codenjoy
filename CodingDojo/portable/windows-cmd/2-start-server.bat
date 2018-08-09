call 00-settings.bat

echo off
echo [44;93m
echo        +-----------------------------------------------+        
echo        !       Now we are starting the server...       !        
echo        ! Also we'll open register page on your browser !        
echo        !   So you can use it for registration after    !        
echo        +-----------------------------------------------+        
echo [0m
echo on
pause >nul

call explorer http://127.0.0.1:8080/%CONTEXT%

cd %JETTY_HOME%
%JAVA_HOME%\bin\java -jar start.jar

echo Press any key to exit
pause >nul

cd %ROOT%