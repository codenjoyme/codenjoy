JETTY_UID=999

# for nginx
chown root:root ./config/nginx/*

# for postgres codenjoy_db
mkdir -p ./content/database
chown root:root ./content/database

# for sqlite
#S# mkdir -p ./content/codenjoy/database
#S# chown $JETTY_UID:$JETTY_UID ./content/codenjoy/database

# for codenjoy_balancer / codenjoy_contest
mkdir -p ./config/codenjoy
chown $JETTY_UID:$JETTY_UID ./config/codenjoy

mkdir -p ./logs/codenjoy
touch ./logs/codenjoy/codenjoy-contest.log
chown $JETTY_UID:$JETTY_UID ./logs/codenjoy/codenjoy-contest.log

