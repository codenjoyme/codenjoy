echo %M2_HOME%
IF "%M2_HOME%"=="" (
    SET M2_HOME=%CD%\..\..\maven
)
IF NOT EXIST "%M2_HOME%" (
    echo "where mvn"
    FOR /F "tokens=*" %%d IN ('where mvn') do (
        SET M2_HOME=%%d
        IF NOT "%M2_HOME%"=="" GOTO:here ;
    )
)
:here
IF NOT EXIST "%M2_HOME%" (
    echo off
    echo [44;93m
    echo        +--------------------------------------------------+
    echo        !              [102;30mPlease install maven[44;93m               !
    echo        +--------------------------------------------------+
    echo [0m
    echo on
    GOTO :EOF
)
IF "%CODENJOY_VERSION%"=="" SET CODENJOY_VERSION=1.0.28

echo CODENJOY_VERSION=%CODENJOY_VERSION%
echo M2_HOME=%M2_HOME%

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
IF "%DEBUG%"=="true" (
    pause >nul
)

call mvn install:install-file -Dfile=games-%CODENJOY_VERSION%-pom.xml -DpomFile=games-%CODENJOY_VERSION%-pom.xml -DgroupId=com.codenjoy -DartifactId=games -Dversion=%CODENJOY_VERSION% -Dpackaging=pom
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