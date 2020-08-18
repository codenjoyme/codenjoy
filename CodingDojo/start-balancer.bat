set ROOT=%CD%

java -jar %ROOT%\balancer\target\codenjoy-balancer.war ^
        --MAVEN_OPTS=-Xmx1024m ^
        --spring.profiles.active=sqlite,debug ^
        --context=/codenjoy-balancer ^
        --server.port=8081 ^
		--game.type=bomberman ^
		--game.servers=localhost:8080 ^
		--room=1 ^
		--start-day=2020-08-18 ^
		--end-day=2020-08-30 ^
		--finalists-count=10 ^
		--final-time=19:00
 
pause >nul