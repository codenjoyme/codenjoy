export ROOT=$PWD

if [ "$CLIENT_ID" == "" ] ; then
	export CLIENT_ID=dojo
fi

if [ "$CLIENT_SECRET" == "" ] ; then
	export CLIENT_SECRET=secret
fi

java -jar "$ROOT/server/target/codenjoy-contest.war" \
        --MAVEN_OPTS=-Xmx1024m \
        --spring.profiles.active=sqlite,oauth2,debug \
        --context=/codenjoy-contest \
		--page.main.unauthorized=false \
        --spring.security.oauth2.client.registration.dojo.redirect-uri-template=http://localhost:3000/codenjoy-contest/login/oauth2/code/dojo \
        --server.forward-headers-strategy=framework \
        --server.port=8080 \
        --allGames \
        --OAUTH2_AUTH_SERVER_URL=http://localhost:3000/api/v1/auth \
        --OAUTH2_AUTH_URI=/protocol/openid-connect/auth \
        --OAUTH2_TOKEN_URI=/protocol/openid-connect/token \
        --OAUTH2_USERINFO_URI=/protocol/openid-connect/userinfo \
        --OAUTH2_CLIENT_ID=$CLIENT_ID \
        --OAUTH2_CLIENT_SECRET=$CLIENT_SECRET \
        --CLIENT_NAME=dojo
