# ---------------------------------------------------------------
# docker build -t apofig/codenjoy-source . --build-arg GAMES=snake,bomberman,sample --build-arg SKIP_TESTS=false
# docker run --name codenjoy -d -p 80:8080 apofig/codenjoy-source
# docker exec -it codenjoy /bin/sh
# docker container stop codenjoy
# -----------------------------------------------------------------

FROM apofig/java-8-jetty-9.3-git-maven-3.5.4
MAINTAINER Alexander Baglay <apofig@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

ARG GIT_REPO=https://github.com/codenjoyme/codenjoy.git
ARG SKIP_TESTS=true
ARG GAMES

RUN cd /tmp \
    && git clone ${GIT_REPO} \
    && cd /tmp/codenjoy/CodingDojo \
    && mvn clean install -DskipTests=${SKIP_TESTS} \
    && touch /var/lib/jetty/LOCAL \ 
    && cp /tmp/codenjoy/CodingDojo/builder/target/codenjoy-contest.war /var/lib/jetty/webapps \
    && cd /tmp/codenjoy/CodingDojo/builder
    
RUN cd /tmp/codenjoy/CodingDojo/builder \
    && if [ "x$GAMES" = "x" ] ; \
            then \
                mvn clean install -DallGames ; \
            else \
                mvn clean install -P${GAMES} ; \
            fi
    
RUN 

VOLUME ["/var/lib/jetty/database"]

EXPOSE 8080