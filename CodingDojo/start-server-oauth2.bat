set ROOT=%CD%

java -jar %ROOT%\server\target\codenjoy-contest.war ^
        --MAVEN_OPTS=-Xmx1024m ^
        --spring.profiles.active=sqlite,sso,debug ^
        --context=/codenjoy-contest ^
        --server.port=8080 ^
        --allGames ^
		--OAUTH2_AUTH_SERVER_URL=https://login-staging.telescopeai.com/core ^
        --OAUTH2_AUTH_URI=/connect/authorize ^
        --OAUTH2_RESOURCE_SERVER_LOCATION=http://localhost:8080/codenjoy-contest ^
        --OAUTH2_CLIENT_ID=dojo ^
        --OAUTH2_CLIENT_SECRET=secret ^
        --OAUTH2_TOKEN_URI=/connect/token ^
        --OAUTH2_USERINFO_URI=/connect/userinfo ^
        --CLIENT_NAME=dojo
 
pause >nul