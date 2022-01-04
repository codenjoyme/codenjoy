@echo off
rem #%L
rem Codenjoy - it's a dojo-like platform from developers to developers.
rem %%
rem Copyright (C) 2012 - 2022 Codenjoy
rem %%
rem This program is free software: you can redistribute it and/or modify
rem it under the terms of the GNU General Public License as
rem published by the Free Software Foundation, either version 3 of the
rem License, or (at your option) any later version.
rem
rem This program is distributed in the hope that it will be useful,
rem but WITHOUT ANY WARRANTY; without even the implied warranty of
rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
rem GNU General Public License for more details.
rem
rem You should have received a copy of the GNU General Public
rem License along with this program.  If not, see
rem <http://www.gnu.org/licenses/gpl-3.0.html>.
rem #L%
@echo on

cd ..

set ROOT=%CD%

IF "%CLIENT_ID%"=="" (
	set CLIENT_ID=dojo
)

IF "%CLIENT_SECRET%"=="" (
	set CLIENT_SECRET=secret
)

cd %ROOT%\server
call %ROOT%\mvnw clean spring-boot:run ^
        -DMAVEN_OPTS=-Xmx1024m ^
        -Dspring.profiles.active=sqlite,oauth2,debug ^
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