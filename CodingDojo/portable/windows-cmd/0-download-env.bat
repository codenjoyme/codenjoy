call 00-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !          Installing Maven           !        
echo        +-------------------------------------+        
echo [0m
echo on

rd /S /Q %M2_HOME%
powershell -command "& { iwr http://apache.ip-connect.vn.ua/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.zip -OutFile %ROOT%\tools\maven.zip }"
%ROOT%\tools\7z x -y -o%ROOT% %ROOT%\tools\maven.zip
rename %ROOT%\apache-maven-3.5.4 maven
mkdir %M2_HOME%\.m2
mkdir %M2_HOME%\.m2\repository

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !           Installing Git            !        
echo        +-------------------------------------+        
echo [0m
echo on

rd /S /Q %GIT_HOME%
mkdir %GIT_HOME%
powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; (new-object System.Net.WebClient).DownloadFile('https://github.com/git-for-windows/git/releases/download/v2.18.0.windows.1/MinGit-2.18.0-64-bit.zip','%ROOT%\tools\git.zip') }"
%ROOT%\tools\7z x -y -o%GIT_HOME% %ROOT%\tools\git.zip

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !          Installing Jetty           !        
echo        +-------------------------------------+        
echo [0m
echo on

rd /S /Q %JETTY_HOME%
powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; (new-object System.Net.WebClient).DownloadFile('https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.3.14.v20161028/jetty-distribution-9.3.14.v20161028.zip','%ROOT%\tools\jetty.zip') }"
%ROOT%\tools\7z x -y -o%ROOT% %ROOT%\tools\jetty.zip
rename %ROOT%\jetty-distribution-9.3.14.v20161028 jetty9

echo off
echo [44;93m
echo        +-------------------------------------+        
echo        !           Installing JDK            !        
echo        +-------------------------------------+        
echo [0m
echo on

rd /S /Q %JAVA_HOME%
mkdir %JAVA_HOME%
mkdir %JAVA_HOME%\tmp
powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; (new-object System.Net.WebClient).DownloadFile('https://download.java.net/java/jdk8u192/archive/b04/binaries/jdk-8u192-ea-bin-b04-windows-x64-01_aug_2018.exe','%ROOT%\tools\jdk.exe') }"
%ROOT%\tools\7z x -y -o%JAVA_HOME%\tmp %ROOT%\tools\jdk.exe
extrac32 %JAVA_HOME%\tmp\.rsrc\1033\JAVA_CAB10\111 -y -l "%JAVA_HOME%"
extrac32 %JAVA_HOME%\tmp\.rsrc\1033\JAVA_CAB9\110 -y -l "%JAVA_HOME%"
rd /S /Q %JAVA_HOME%\tmp
%ROOT%\tools\7z x -y -o%JAVA_HOME% %JAVA_HOME%\tools.zip
del /F %JAVA_HOME%\tools.zip
cd %JAVA_HOME%
for /r %%x in (*.pack) do %JAVA_HOME%\bin\unpack200 -r "%%x" "%%~dx%%~px%%~nx.jar"

cd %ROOT%

echo off
echo [44;93m
echo        +---------------------------------------------+        
echo        !      All build files are cleaned            !        
echo        !      Please run [102;30m1-rebuild-server.bat[44;93m        !        
echo        +---------------------------------------------+        
echo [0m
echo on
pause >nul