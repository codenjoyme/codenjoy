set ROOT=%CD%

IF "%CLIENT_ID%"=="" (
	set CLIENT_ID=dojo
)

IF "%CLIENT_SECRET%"=="" (
	set CLIENT_SECRET=secret
)

call %ROOT%\mvnw mvn -f %ROOT%\server\pom.xml ^
        -DMAVEN_OPTS=-Xmx1024m ^
        -Dspring.profiles.active=sqlite,oauth2,debug,trace ^
        -Dcontext=/codenjoy-contest ^
		-Dpage.main.unauthorized=false ^
        -Dspring.security.oauth2.client.registration.dojo.redirect-uri-template=http://localhost:3000/codenjoy-contest/login/oauth2/code/dojo ^
        -Dserver.forward-headers-strategy=framework ^
        -Dserver.port=8080 ^
        -DallGames ^
        -DOAUTH2_AUTH_SERVER_URL=http://localhost:3000/api/v1/auth ^
        -DOAUTH2_AUTH_URI=/protocol/openid-connect/auth ^
        -DOAUTH2_TOKEN_URI=/protocol/openid-connect/token ^
        -DOAUTH2_USERINFO_URI=/protocol/openid-connect/userinfo ^
        -DOAUTH2_CLIENT_ID=%CLIENT_ID% ^
        -DOAUTH2_CLIENT_SECRET=%CLIENT_SECRET% ^
        -DCLIENT_NAME=dojo
 
pause >nul