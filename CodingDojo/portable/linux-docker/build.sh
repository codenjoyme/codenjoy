sudo docker build --target javaworkspace -t java-workspace .
sudo docker build --target codenjoysource -t codenjoy-source . # --build-arg GIT_REPO=https://github.com/codenjoyme/codenjoy.git --build-arg REVISION=7a1cbd8 --build-arg SKIP_TESTS=false
sudo docker build --target codenjoydeploy -t codenjoy-deploy . # --build-arg CONTEXT=codenjoy-contest --build-arg GAMES=snake,bomberman,sample
sudo docker run --name codenjoy -d -p 80:8080 codenjoy-deploy
# docker exec -it codenjoy /bin/sh
# docker container stop codenjoy