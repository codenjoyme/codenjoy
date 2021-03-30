FROM openjdk:11

ARG SPRING_PROFILES=sqlite
ARG CODENJOY_CONTEXT=codenjoy-contest

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN ./mvnw clean package -D allGames -D skipTests
ENV SPRING_PROFILES=${SPRING_PROFILES}
ENV CODENJOY_CONTEXT=${CODENJOY_CONTEXT}

EXPOSE 8080
CMD ["java", "-jar", "./server/target/codenjoy-contest.war", "--spring.profiles.active=${SPRING_PROFILES}", "--context=/${CODENJOY_CONTEXT}"]
