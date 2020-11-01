FROM maven:3.6.3-jdk-8

ADD /CodingDojo /build
WORKDIR /build

RUN mvn clean package -D allGames -D skipTests


FROM openjdk:11.0.9-jre-slim

ARG SPRING_PROFILES=sqlite
ARG CODENJOY_CONTEXT=codenjoy-contest

COPY --from=0 "/build/server/target/codenjoy-contest.war .

EXPOSE 8080

CMD ["java", "-jar", "./codenjoy-contest.war", "--spring.profiles.active=${SPRING_PROFILES}", "--context=/${CODENJOY_CONTEXT}"]
