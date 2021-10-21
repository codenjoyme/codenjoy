call 0-settings.bat

echo off
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo        [44;93m!                   Now we are building clients...                        ![0m
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo on

cd %LANGUAGE%
call 2-build.bat
cd %CLIENTS_ROOT%