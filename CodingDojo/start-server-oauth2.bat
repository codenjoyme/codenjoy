set ROOT=%CD%

java -jar %ROOT%\server\target\codenjoy-contest.war ^
        --MAVEN_OPTS=-Xmx1024m ^
        --spring.profiles.active=sqlite,oauth2,debug,trace ^
        --context=/codenjoy-contest ^
        --spring.security.oauth2.client.registration.dojo.redirect-uri-template=http://localhost:3000/codenjoy-contest/login/oauth2/code/dojo ^
        --server.forward-headers-strategy=framework ^
        --server.port=8080 ^
        --allGames ^
        --OAUTH2_AUTH_SERVER_URL=http://localhost:3000/api/v1/auth ^
        --OAUTH2_AUTH_URI=/protocol/openid-connect/auth ^
        --OAUTH2_TOKEN_URI=/protocol/openid-connect/token ^
        --OAUTH2_USERINFO_URI=/protocol/openid-connect/userinfo ^
        --OAUTH2_CLIENT_ID=dojo ^
        --OAUTH2_CLIENT_SECRET=secret ^
        --CLIENT_NAME=dojo
 
pause >nul