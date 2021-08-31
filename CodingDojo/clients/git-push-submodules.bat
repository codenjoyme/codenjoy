git config --global credential.helper wincred

git flow feature publish

cd java
git push origin master

cd ../pseudo
git push origin master

cd ../java-script
git push origin master

cd ../ruby
git push origin master

cd ../go
git push origin master

cd ..

git config --global --unset  credential.helper