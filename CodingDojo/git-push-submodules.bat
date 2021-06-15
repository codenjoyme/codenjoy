git config --global credential.helper wincred

git push origin
git submodule foreach git push origin master

git config --global --unset  credential.helper