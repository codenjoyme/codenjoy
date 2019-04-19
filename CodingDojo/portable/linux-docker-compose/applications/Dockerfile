FROM openjdk:8-jdk as java_workspace
MAINTAINER Alexander Baglay <apofig@gmail.com>

USER root
ENV DEBIAN_FRONTEND noninteractive

ARG TIMEZONE=Europe/Kiev

RUN apt-get update \
 && ln -sf /usr/share/zoneinfo/${TIMEZONE} /etc/localtime \
 && dpkg-reconfigure -f noninteractive tzdata

# -----------------------------------------------------------------------

FROM java-workspace as codenjoy_source
MAINTAINER Alexander Baglay <apofig@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

ARG GIT_REPO=https://github.com/codenjoyme/codenjoy.git
ARG REF=master

RUN apt-get update \
 && apt-get -y install -y git \
 && git config --global user.name "Alexander Baglay" \
 && git config --global user.email "apofig@gmail.com" \
 && cd /tmp \
 && git clone ${GIT_REPO} \
 && cd /tmp/codenjoy/CodingDojo \
 && git checkout ${REF}

# -----------------------------------------------------------------------
