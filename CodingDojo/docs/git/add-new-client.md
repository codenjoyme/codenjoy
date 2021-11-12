```bash
# на примере csharp клиента который размещен 
# в https://github.com/codenjoyme/codenjoy-csharp-client.git
# добавим его сюда https://github.com/codenjoyme/codenjoy/tree/master/CodingDojo/clients  

cd /CodingDojo/clients
git submodule add https://github.com/codenjoyme/codenjoy-csharp-client.git ./csharp

# после стоит обновить https
cd ../../.git/
cat config
# ...
# [submodule "CodingDojo/clients/csharp"]
#    url = https://github.com/codenjoyme/codenjoy-csharp-client.git

cd modules/CodingDojo/clients/csharp
cat config
# [core]
#     repositoryformatversion = 0
#     filemode = false
#     bare = false
#     logallrefupdates = true
#     symlinks = false
#     ignorecase = true
#     worktree = ../../../../../CodingDojo/clients/csharp
# [remote "origin"]
#     url = https://github.com/codenjoyme/codenjoy-csharp-client.git
#     fetch = +refs/heads/*:refs/remotes/origin/*
# [branch "master"]
#     remote = origin
#     merge = refs/heads/master

git ../../../../..
git add CodingDojo/clients/csharp
git add .gitmodules
git checkout -B feature/new-csharp-client
git commit -m "[clients][csharp] Added new client based on game version."
git push origin feature/new-csharp-client

# change url to ssh
# url = git@github.com:codenjoyme/codenjoy-csharp-client.git
```