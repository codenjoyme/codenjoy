call mvn clean assembly:assembly
call rmdir /S /Q .\out\*.*
call md .\out
call copy .\target\*.jar .\out\*.*
call copy .\run-client.bat .\out\*.*
call explorer .\out