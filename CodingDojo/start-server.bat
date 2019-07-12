set ROOT=%CD%

cd %ROOT%\server
call %ROOT%\mvnw clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug -Dcontext=/codenjoy-contest -Djetty.http.port=8081 -DallGames
rem call %ROOT%\mvnw clean spring-boot:run -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,debug -Dcontext=/codenjoy-contest -Djetty.http.port=8080 -Psnake,bomberman,sample

pause >nul