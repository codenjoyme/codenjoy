call mvn -DaltDeploymentRepository=snapshots::default::file:repo\snapshots clean deploy 1>deploy-snapshot.log 2>&1
pause >nul