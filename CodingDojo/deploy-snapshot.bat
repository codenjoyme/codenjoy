call mvnw -DaltDeploymentRepository=snapshots::default::file:repo\snapshots clean deploy -DskipTests=true -DgitDir=.\..\ > deploy-snapshot.log

pause >nul