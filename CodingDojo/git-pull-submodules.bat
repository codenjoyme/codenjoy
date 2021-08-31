git config --global credential.helper wincred

for /f %%i in ('git describe --contains --all HEAD') do set BRANCH=%%i

git pull origin %BRANCH%
git pull origin develop

git submodule foreach git checkout -b temp

git submodule update --remote --init

git submodule foreach git checkout -b temp2
git submodule foreach git checkout master
git submodule foreach git merge temp
git submodule foreach git merge temp2
git submodule foreach git branch -d temp
git submodule foreach git branch -d temp2

git submodule foreach git pull origin master

git config --global --unset  credential.helper