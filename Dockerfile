FROM openjdk:11

ENV SPRING_PROFILES=sqlite
ENV CODENJOY_CONTEXT=codenjoy-contest

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN ./mvnw clean package -D allGames -D skipTests

EXPOSE 8080
CMD ["java", "-jar", "./server/target/codenjoy-contest.war", "--spring.profiles.active=${SPRING_PROFILES}", "--context=/${CODENJOY_CONTEXT}"]