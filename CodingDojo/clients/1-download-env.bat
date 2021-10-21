call 0-settings.bat

echo off
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo        [44;93m!                   Now we are downloading stuff...                       ![0m
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo on

cd %LANGUAGE%
call 1-download-env.bat
cd %CLIENTS_ROOT%