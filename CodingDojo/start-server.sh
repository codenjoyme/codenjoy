java -jar ./server/target/codenjoy-contest.war \
        --MAVEN_OPTS=-Xmx1024m \
        --spring.profiles.active=sqlite,debug \
        --context=/codenjoy-contest \
        --server.port=8080