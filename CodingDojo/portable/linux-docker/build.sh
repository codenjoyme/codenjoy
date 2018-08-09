docker build --target javaworkspace -t apofig/java-workspace .
docker build --target codenjoysource -t apofig/codenjoy-source . # --build-arg GIT_REPO=https://github.com/codenjoyme/codenjoy.git --build-arg REVISION=7a1cbd8 --build-arg SKIP_TESTS=false
docker build --target codenjoydeploy -t apofig/codenjoy-deploy . # --build-arg GAMES=snake,bomberman,sample
docker run --name codenjoy -d -p 80:8080 apofig/codenjoy-deploy
# docker exec -it codenjoy /bin/sh
# docker container stop codenjoy