SET KATA_PROFILE=finale
rem SET KATA_PROFILE=default
SET MAVEN_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000 -Xmx1024m -Dmaven.test.skip=true -Djetty.http.port=8081
call mvn clean jetty:run-war
