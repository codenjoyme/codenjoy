call mvnw -DaltDeploymentRepository=snapshots::default::file:repo\snapshots clean deploy -DskipTests=true > deploy-snapshot.log

pause >nul