FROM java-workspace
MAINTAINER Alexander Baglay <apofig@gmail.com>

ARG WAR_FILE=codenjoy-balancer.war
ARG SPRING_PROFILES=sqlite
ARG CODENJOY_CONTEXT=codenjoy-balancer

RUN mkdir -p /app

COPY ${WAR_FILE} /app/server.war

WORKDIR /app

ENTRYPOINT ["java","-jar","server.war"]
CMD ["--spring.profiles.active=${SPRING_PROFILES}","--context=/${CODENJOY_CONTEXT}"]
