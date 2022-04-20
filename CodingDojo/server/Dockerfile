FROM openjdk:11
ADD target/codenjoy-contest.war app.war
EXPOSE 8080
CMD ["java", "-jar", "app.war", "--spring.profiles.active=postgres,bomberman,debug", "--CODE_EXECUTION_HOST=code-execution", "--NOTIFICATIONS_HOST=notifications", "--KEYCLOACK_HOST=keycloak", "--POSTGRES_HOST=host.docker.internal", "--KEYCLOACK_PORT=8080"]