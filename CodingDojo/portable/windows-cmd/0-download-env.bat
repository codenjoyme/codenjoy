call 00-settings.bat

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
echo        !           Installing JDK            !        
echo        +-------------------------------------+        
echo [0m
echo on

rd /S /Q %JAVA_HOME%
mkdir %JAVA_HOME%
mkdir %JAVA_HOME%\tmp
powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; $client=New-Object System.Net.WebClient; $client.Headers.Add([System.Net.HttpRequestHeader]::Cookie, 'oraclelicense=accept-securebackup-cookie'); $client.DownloadFile('https://download.oracle.com/otn-pub/java/jdk/8u201-b09/42970487e3af4f5aa5bca3f542482c60/jdk-8u201-windows-x64.exe','%ROOT%\tools\jdk.exe') }"
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