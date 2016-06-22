mvn -DaltDeploymentRepository=snapshots::default::file:%1\snapshots clean deploy
cd %1
git add *
git commit -m "deploy codenjoy"
git push origin master