#!/usr/bin/env sh

DB_DIR=/data/dojoserver

mkdir -p $DB_DIR

docker run \
        -d \
        --name dojorena-game-server \
        -e GAME_AI=true \
        -e SPRING_PROFILES_ACTIVE=sqlite,debug,icancode \
        -v $DB_DIR:/usr/app/database \
        -p 8080:8080 \
        dojorena/game-server:1.1.0
igor_petrov@epam.com@ecsc00a04d45:/code/codenjoy/CodingDojo$ cat portable/Dockerfile
######################################################
#           Game Server project build
######################################################
FROM maven:3.6.1-jdk-8 as BUILD
MAINTAINER Igor_Petrov@epam.com

ENV MVN_HOME=/home/maven

# comma-separated list of games or 'allGames'
ENV GAMES=allGames

WORKDIR $MVN_HOME

COPY games ./games
COPY server ./server
COPY balancer ./balancer
COPY utilities ./utilities
COPY pom.xml ./pom.xml

RUN mvn clean package -D$GAMES -DskipTests

######################################################
#                 Game Server image
######################################################
FROM openjdk:8 as SERVER
ENV APP_HOME=/usr/app

ENV ARTIFACT_NAME=codenjoy-contest.war

ARG PROFILES="sqlite,debug,icancode"
ENV PROFILES=${PROFILES}

ARG SERVER_CONTEXT="codenjoy-contest"
ENV SERVER_CONTEXT=${SERVER_CONTEXT}

WORKDIR $APP_HOME

COPY --from=BUILD /home/maven/server/target/$ARTIFACT_NAME .

EXPOSE 8080

ENTRYPOINT java -jar $ARTIFACT_NAME

CMD "--spring.profiles.active=${PROFILES} --context=/${SERVER_CONTEXT}"
