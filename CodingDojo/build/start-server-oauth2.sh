#!/usr/bin/env bash

###
# #%L
# Codenjoy - it's a dojo-like platform from developers to developers.
# %%
# Copyright (C) 2012 - 2022 Codenjoy
# %%
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public
# License along with this program.  If not, see
# <http://www.gnu.org/licenses/gpl-3.0.html>.
# #L%
###

cd ..

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
