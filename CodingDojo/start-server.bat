set ROOT=%CD%

cd %ROOT%\server
call %ROOT%\mvnw -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,trace clean spring-boot:run -Dcontext=/codenjoy-context -DallGames 
rem call %ROOT%\mvnw -DMAVEN_OPTS=-Xmx1024m -Dmaven.test.skip=true -Dspring.profiles.active=sqlite,trace clean spring-boot:run -Dcontext=/codenjoy-context -Psnake,bomberman,sample

pause >nul