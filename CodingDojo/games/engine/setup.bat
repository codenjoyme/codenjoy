call mvn install:install-file -Dfile=engine-1.0.23.jar -Dsources=engine-1.0.23-sources.jar -DpomFile=engine-1.0.23-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=1.0.23 -Dpackaging=jar
echo [INFO] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo [INFO] !!!!    Check that maven prints BUILD SUCCESS
echo [INFO] !!!!    Then press Enter to continue (and wait until the next step installation is complete)
echo [INFO] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
pause >nul
call mvn install:install-file -Dfile=games-1.0.23-pom.xml -DpomFile=games-1.0.22-pre-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=1.0.22b -Dpackaging=pom
echo [INFO] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
echo [INFO] !!!!    Check that maven prints BUILD SUCCESS
echo [INFO] !!!!    Then press Enter to exit
echo [INFO] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
pause >nul