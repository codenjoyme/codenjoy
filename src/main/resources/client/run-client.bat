@echo off
set /p clientlanguage="[java|kotlin]?"
echo %clientlanguage%
call java -jar icancode-engine.jar %clientlanguage%