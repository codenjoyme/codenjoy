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
IF "%DEBUG%"=="true" ( 
    pause >nul
)

call explorer http://127.0.0.1:8080%CONTEXT%

%JAVA_HOME%\bin\java -jar %APP_HOME%\server.war --spring.profiles.active=%SPRING_PROFILES% --context=%CONTEXT%

echo Press any key to exit
pause >nul

cd %ROOT%

call explorer http://127.0.0.1:8080%CONTEXT%
