FROM openjdk:8

WORKDIR /usr/src/app
COPY ./CodingDojo .

RUN ./mvnw clean package -D allGames -D skipTests

EXPOSE 8080
CMD ["java", "-jar", "./target/codenjoy-contest.war", "--spring.profiles.active=sqlite,debug,sso", "--context=/codenjoy-contest"]
