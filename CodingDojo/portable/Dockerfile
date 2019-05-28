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
ENV ARTIFACT_VERSION=1.1.0
ENV ARTIFACT_NAME=codenjoy-contest.war
ENV PROFILES="sqlite,debug,icancode"
ENV SERVER_CONTEXT="codenjoy-contest"

WORKDIR $APP_HOME

COPY --from=BUILD server/target/$ARTIFACT_NAME .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "$ARTIFACT_NAME"]

CMD ["--spring.profiles.active=${PROFILES}", "--context=/${SERVER_CONTEXT}"]
