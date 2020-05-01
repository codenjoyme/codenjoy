set ROOT=%CD%

java -jar %ROOT%\server\target\codenjoy-contest.war ^
        --MAVEN_OPTS=-Xmx1024m ^
        --spring.profiles.active=sqlite,debug ^
        --context=/codenjoy-contest ^
        --server.port=8080 ^
        --plugins.resources=./external/a2048-engine.jar!/resources/
 
pause >nul