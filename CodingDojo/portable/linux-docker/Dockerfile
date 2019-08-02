######################################################
#           Game Server project build
######################################################
FROM maven:3.6.1-jdk-8 as BUILD
MAINTAINER Igor_Petrov@epam.com

ENV MVN_HOME=/home/maven

# comma-separated list of games or 'allGames'
# TODO for all games sould be -P parameter in 'mvn clean package' command
ENV GAMES=allGames

WORKDIR $MVN_HOME

COPY ./codenjoy/CodingDojo/games ./games
COPY ./codenjoy/CodingDojo/server ./server
COPY ./codenjoy/CodingDojo/balancer ./balancer
COPY ./codenjoy/CodingDojo/utilities ./utilities
COPY ./codenjoy/CodingDojo/pom.xml ./pom.xml

RUN mvn clean package -D$GAMES -DskipTests

######################################################
#                 Game Server image
######################################################
FROM openjdk:8 as SERVER
MAINTAINER Igor_Petrov@epam.com

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
