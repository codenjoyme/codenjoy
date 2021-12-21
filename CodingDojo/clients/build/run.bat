if "%LANGUAGE%"=="" ( set LANGUAGE=java)
cd ./../%LANGUAGE%/build
call run.bat

echo Press Enter to continue
read