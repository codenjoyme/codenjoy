git submodule update --remote --init

git submodule foreach git checkout develop
git submodule foreach git merge origin/develop
git submodule foreach git checkout master
git submodule foreach git merge origin/master
git submodule foreach git checkout develop
git submodule foreach git merge master
git submodule foreach git checkout master
git submodule foreach git merge develop