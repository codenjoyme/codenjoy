set ROOT=%CD%

java -jar %ROOT%\server\target\codenjoy-contest.war ^
        --MAVEN_OPTS=-Xmx1024m ^
        --spring.profiles.active=sqlite,sso,debug ^
        --context=/codenjoy-contest ^
        --server.port=8080 ^
        --allGames ^
        --OAUTH2_AUTH_SERVER_URL=http://localhost:3000/api/v1/auth  ^
        --OAUTH2_AUTH_URI=/protocol/openid-connect/auth ^
        --OAUTH2_RESOURCE_SERVER_LOCATION=http://localhost:8080/codenjoy-contest ^
        --OAUTH2_CLIENT_ID=dojo ^
        --OAUTH2_CLIENT_SECRET=secret ^
        --OAUTH2_TOKEN_URI=/protocol/openid-connect/token ^
        --OAUTH2_USERINFO_URI=/protocol/openid-connect/userinfo ^
        --CLIENT_NAME=dojo
 
pause >nul