FROM maven:3.8.1-openjdk-11-slim AS codenjoy

ENV SPRING_PROFILES=kata,postgres,debug
ENV CODENJOY_CONTEXT=codenjoy-contest
ENV POSTGRES_HOST="172.20.0.1"
ENV GAMESERVER_HOST="172.20.0.1"

WORKDIR app
COPY ./CodingDojo .

RUN mvn -f ./games/engine/pom.xml install
RUN mvn -f ./games/kata/pom.xml install
RUN mvn -f ./messages/pom.xml install
RUN mvn -f ./common/pom.xml install
RUN mvn -f ./server/pom.xml package -D skipTests -P kata

EXPOSE 8080
CMD ["java", "-jar", "./server/target/codenjoy-contest.jar", "--spring.profiles.active=${SPRING_PROFILES}", "--context=/${CODENJOY_CONTEXT}", "--postgres.host=${POSTGRES_HOST}", "--gameserver.host=${GAMESERVER_HOST}"]