IF "%JAVA_HOME%"=="" set JAVA_HOME=%CD%\..\..\jdk8
IF "%M2_HOME%"=="" set M2_HOME=%CD%\..\..\maven
IF "%CODENJOY_VERSION%"=="" set CODENJOY_VERSION=1.0.26

call %M2_HOME%\bin\mvn install:install-file -Dfile=engine-%CODENJOY_VERSION%.jar -Dsources=engine-%CODENJOY_VERSION%-sources.jar -DpomFile=engine-%CODENJOY_VERSION%-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=%CODENJOY_VERSION% -Dpackaging=jar

echo off
echo [44;93m
echo        +--------------------------------------------------+        
echo        !           Check that BUILD was [102;30mSUCCESS[44;93m           !        
echo        !      Then press Enter to continue (and wait      !        
echo        !   until the next step installation is complete)  !        
echo        +--------------------------------------------------+        
echo [0m
echo on
pause >nul

call mvn install:install-file -Dfile=games-%CODENJOY_VERSION%-pom.xml -DpomFile=games-%CODENJOY_VERSION%-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=%CODENJOY_VERSION% -Dpackaging=pom
echo off
echo [44;93m
echo        +--------------------------------------------------+        
echo        !         Check that BUILD was [102;30mSUCCESS[44;93m             !        
echo        !           Then press Enter to exit               !        
echo        +--------------------------------------------------+        
echo [0m
echo on
pause >nul