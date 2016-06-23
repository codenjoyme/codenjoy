mvn -DaltDeploymentRepository=snapshots::default::file:repo\snapshots clean deploy
cd %1
git add *
git commit -m "deploy codenjoy"
git push origin master