FROM openjdk:8

ARG SPRING_PROFILES=sqlite
ARG CODENJOY_CONTEXT=codenjoy-contest

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN ./mvnw clean package -D allGames -D skipTests

EXPOSE 8080
CMD ["java", "-jar", "./target/codenjoy-contest.war", "--spring.profiles.active=${SPRING_PROFILES}", "--context=/${CODENJOY_CONTEXT}"]
