# Game Server project build
FROM maven:3.6.1-jdk-8 as BUILD
MAINTAINER Igor_Petrov@epam.com

ENV APP_HOME=/home/maven
ENV GAMES=allGames

WORKDIR $APP_HOME

COPY games ./games
COPY server ./server
COPY balancer ./balancer
COPY utilities ./utilities
COPY pom.xml ./pom.xml

RUN mvn clean package -D$GAMES -DskipTests


# Game Server image
FROM openjdk:8
ENV ARTIFACT_VERSION=1.1.0
ENV ARTIFACT_NAME=codenjoy-contest.war
ENV PROFILES="sqlite,debug,icancode"
ENV SERVER_CONTEXT="codenjoy-contest"

WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME/target/$ARTIFACT_NAME /

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "$ARTIFACT_NAME"]

CMD ["--spring.profiles.active=${PROFILES}", "--context=/${SERVER_CONTEXT}"]
