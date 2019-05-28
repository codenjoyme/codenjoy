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
