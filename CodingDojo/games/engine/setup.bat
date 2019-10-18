IF "%CODENJOY_VERSION%"=="" SET CODENJOY_VERSION=1.1.1

echo CODENJOY_VERSION=%CODENJOY_VERSION%

mkdir .\.mvn
mkdir .\.mvn\wrapper

SET MVNW=%cd%\mvnw

IF NOT EXIST ".\engine-$CODENJOY_VERSION-pom.xml" (
    mkdir .\target
    copy .\engine-%CODENJOY_VERSION%-pom.xml .\target\engine-%CODENJOY_VERSION%-pom.xml
    copy .\games-%CODENJOY_VERSION%-pom.xml .\target\games-%CODENJOY_VERSION%-pom.xml
    copy .\*.jar .\target\*.jar
) else (
    IF NOT EXIST ".\target" (
        call %MVNW% clean install -DskipTests=true
        copy .\pom.xml .\target\engine-%CODENJOY_VERSION%-pom.xml
        copy ..\pom.xml .\target\games-%CODENJOY_VERSION%-pom.xml
    )
)

cd .\target

call %MVNW% install:install-file -Dfile=engine-%CODENJOY_VERSION%.jar -Dsources=engine-%CODENJOY_VERSION%-sources.jar -DpomFile=engine-%CODENJOY_VERSION%-pom.xml -DgroupId=com.codenjoy -DartifactId=engine -Dversion=%CODENJOY_VERSION% -Dpackaging=jar

echo off
echo [44;93m
echo        +--------------------------------------------------+        
echo        !           Check that BUILD was [102;30mSUCCESS[44;93m           !        
echo        !      Then press Enter to continue (and wait      !        
echo        !   until the next step installation is complete)  !        
echo        +--------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" (
    pause >nul
)

call %MVNW% install:install-file -Dfile=games-%CODENJOY_VERSION%-pom.xml -DpomFile=games-%CODENJOY_VERSION%-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=%CODENJOY_VERSION% -Dpackaging=pom
echo off
echo [44;93m
echo        +--------------------------------------------------+        
echo        !         Check that BUILD was [102;30mSUCCESS[44;93m             !        
echo        !           Then press Enter to exit               !        
echo        +--------------------------------------------------+        
echo [0m
echo on
IF "%DEBUG%"=="true" (
    pause >nul
)
pause >nul