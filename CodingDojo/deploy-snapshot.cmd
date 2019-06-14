call mvn -DaltDeploymentRepository=snapshots::default::file:repo\snapshots clean deploy -DskipTests=true
pause >nul