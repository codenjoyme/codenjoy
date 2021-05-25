FROM openjdk:11

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN ./mvnw clean package -D allGames -D skipTests

EXPOSE 8080
ENTRYPOINT ["java","-jar","./server/target/codenjoy-contest.war"]