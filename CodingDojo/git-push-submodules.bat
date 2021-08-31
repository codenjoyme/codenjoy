git config --global credential.helper wincred

for /f %%i in ('git describe --contains --all HEAD') do set BRANCH=%%i

git push origin %BRANCH%
git submodule foreach git push origin master

git config --global --unset  credential.helper