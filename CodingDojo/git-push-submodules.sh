unset password
prompt="Enter Password:"
while IFS= read -p "$prompt" -r -s -n 1 char
do
    if [[ $char == $'\0' ]]
    then
        break
    fi
    prompt='*'
    password+="$char"
done

cd ./games/excitebike
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-excitebike.git master 
cd ./../..

cd ./games/japanese
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-japanese.git master 
cd ./../..

cd ./games/selfdefense
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-selfdefense.git master 
cd ./../..

cd ./games/vacuum
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-vacuum.git master
cd ./../..

cd ./games/xonix
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-xonix.git master
cd ./../..

cd ./client-runner
git push https://github.com:$password/codenjoyme/codenjoy-client-runner.git master
cd ./..

cd ./portable/linux-docker
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-portable-linux-lite.git master 
cd ./../..

cd ./portable/linux-docker-compose
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-portable-linux.git master 
cd ./../..

cd ./portable/windows-cmd
git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy-portable-windows.git master 
cd ./../..

git push https://codenjoyme:$password@github.com/codenjoyme/codenjoy.git master 
