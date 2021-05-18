FROM maven:3.8.1-openjdk-11-slim AS codenjoy

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN mvn -f ./games/engine/pom.xml install
RUN mvn -f ./games/kata/pom.xml install
RUN mvn -f ./messages/pom.xml install
RUN mvn -f ./common/pom.xml install
RUN mvn -f ./server/pom.xml package -D skipTests -P kata

EXPOSE 8080
CMD ["java", "-jar", "./server/target/codenjoy-contest.jar", "--spring.profiles.active=kata,postgres,debug", "--context=/codenjoy-contest"]