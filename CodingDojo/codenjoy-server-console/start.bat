# call setx /m JAVA_HOME C:\java\jdk1.8.0_73
# call mvn -X -e clean package
call mvn clean package
call java -jar target/codenjoy-server-console-1.0.20.jar
pause >nul

