#!/usr/bin/env bash

eval_echo() {
    echo "[92m"
    echo $1
    echo "[0m"

    eval $1
}

eval_echo2() {
    echo "[36m"$1"[0m"

    eval $1
}

eval_echo ". env-read.sh"

comment() {
    file=$1
    marker=$2
    flag=$3
    if [ "x$flag" = "xtrue" ]; then
        eval_echo2 "sed -i '/$marker/s/^#\+//' $file"
    else
        eval_echo2 "sed -i '/$marker/s/^#\?/#/' $file"
    fi
    cat $file | grep $marker
}

parameter() {
    file=$1
    before=$2
    value=$3
    after=$4
    if [ "x$after" = "x" ]; then
        eol="\$"
    fi
    eval_echo2 "sed -i 's,\($before\).*$after$eol,\1$value$after,' $file"
    cat $file | grep "$before"
}

# -------------------------- DOMAIN --------------------------

eval_echo "parameter ./config/nginx/domain.conf 'server_name ' $SERVER_IP ';'"

if [ "x$DOMAIN" = "xfalse" ]; then
    SERVER_DOMAIN=$SERVER_IP
fi

eval_echo "parameter ./config/nginx/domain.conf 'return 301 https\?://' $SERVER_DOMAIN '\\$'"

eval_echo "parameter ./config/nginx/conf.d/codenjoy-balancer.conf 'server_name ' $SERVER_DOMAIN ';'"
eval_echo "parameter ./config/nginx/conf.d/codenjoy-contest.conf 'server_name ' $SERVER_DOMAIN ';'"

domain() {
    file=$1
    comment $file "#D#" $DOMAIN
}

eval_echo "domain ./config/nginx/nginx.conf"

# -------------------------- OPEN PORTS --------------------------

ports() {
    file=$1
    comment $file "#P#" $OPEN_PORTS
}

eval_echo "ports ./docker-compose.yml"
eval_echo "ports ./codenjoy.yml"
eval_echo "ports ./balancer.yml"
eval_echo "ports ./wordpress.yml"

# -------------------------- BASIC AUTH --------------------------

basic_auth() {
    file=$1
    comment $file "#A#" $BASIC_AUTH
}

eval_echo "basic_auth ./config/nginx/conf.d/codenjoy-balancer.conf"
eval_echo "basic_auth ./config/nginx/conf.d/codenjoy-contest.conf"
eval_echo "basic_auth ./config/nginx/conf.d/wordpress/locations.conf"

# -------------------------- SSL --------------------------

ssl() {
    file=$1
    comment $file "#S#" $SSL
    if [ "x$SSL" = "xtrue" ]; then
        NOT_SSL="false";
    else
        NOT_SSL="true";
    fi
    comment $file "#!S#" $NOT_SSL
}

eval_echo "ssl ./config/nginx/domain.conf"
eval_echo "ssl ./config/nginx/conf.d/codenjoy-balancer.conf"
eval_echo "ssl ./config/nginx/conf.d/codenjoy-contest.conf"
eval_echo "ssl ./docker-compose.yml"

eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'database.name=' $CODENJOY_POSTGRES_NAME"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'database.user=' $CODENJOY_POSTGRES_USER"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'database.password=' $CODENJOY_POSTGRES_PASSWORD"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'admin.password=' $ADMIN_PASSWORD"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'email.hash=' $EMAIL_HASH"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'game.type=' $GAME"
eval_echo "parameter ./config/codenjoy/codenjoy-balancer.properties 'game.servers=' $BALANCER_GAME_SERVERS"

eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'database.name=' $CODENJOY_POSTGRES_NAME"
eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'database.user=' $CODENJOY_POSTGRES_USER"
eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'database.password=' $CODENJOY_POSTGRES_PASSWORD"
eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'admin.password=' $ADMIN_PASSWORD"
eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'email.hash=' $EMAIL_HASH"
eval_echo "parameter ./config/codenjoy/codenjoy-contest.properties 'server.ip=' $SERVER_IP"

# -------------------------- DATABASE --------------------------

database() {
    file=$1
    if [ "x$DATABASE_TYPE" = "xpostgre" ]; then
        POSTGRE="true";
        SQLITE="false";
    else
        POSTGRE="false";
        SQLITE="true";
    fi
    comment $file "#L#" $SQLITE
    comment $file "#!L#" $POSTGRE

    # TODO to solve situation with multiple tags #!LP#
    if [ "x$POSTGRE" = "xtrue" ] && [ "x$OPEN_PORTS" = "xtrue" ]; then
        comment $file "#!LP#" "true"
    else
        comment $file "#!LP#" "false"
    fi
}

eval_echo "database ./docker-compose.yml"
eval_echo "database ./balancer.yml"
eval_echo "database ./codenjoy.yml"

# -------------------------- WORDPRESS --------------------------

wordpress() {
    file=$1
    comment $file "#W#" $WORDPRESS
    if [ "x$WORDPRESS" = "xtrue" ]; then
        NOT_WORDPRESS="false";
    else
        NOT_WORDPRESS="true";
    fi
    comment $file "#!W#" $NOT_WORDPRESS

    # TODO to solve situation with multiple tags #S# #!W#
    if [ "x$NOT_WORDPRESS" = "xtrue" ]; then
        eval_echo "ssl ./config/nginx/conf.d/codenjoy-contest.conf"
    fi
}

eval_echo "wordpress ./config/nginx/conf.d/codenjoy-contest.conf"

# --------------------------         --------------------------