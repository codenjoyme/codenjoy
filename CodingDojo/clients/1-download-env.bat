call 0-settings.bat

echo off
echo [44;93m
echo        +-------------------------------------+
echo        !           Installing JDK            !
echo        +-------------------------------------+
echo [0m
echo on

rd /S /Q %JAVA_HOME%
powershell -command "& { set-executionpolicy remotesigned -s currentuser; [System.Net.ServicePointManager]::SecurityProtocol = 3072 -bor 768 -bor 192 -bor 48; $client=New-Object System.Net.WebClient; $client.Headers.Add([System.Net.HttpRequestHeader]::Cookie, 'oraclelicense=accept-securebackup-cookie'); $client.DownloadFile('https://aka.ms/download-jdk/microsoft-jdk-11.0.11.9.1-windows-x64.zip','%TOOLS%\jdk.zip') }"
%TOOLS%\7z x -y -o%ROOT% %TOOLS%\jdk.zip
rename jdk-11.0.11+9 .jdk
cd %ROOT%

echo off
echo [44;93m
echo        +-------------------------------------+
echo        !     Now you can run 2-build.bat     !
echo        +-------------------------------------+
echo [0m
echo on

echo Press any key to exit
pause >nul