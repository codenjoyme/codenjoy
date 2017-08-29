@echo off
set /p clientlanguage="[java|kotlin]?"
echo %clientlanguage%
call java -jar expansion-engine.jar %clientlanguage%