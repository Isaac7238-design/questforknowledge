@echo off
echo Starting Lucienne: Quest for Quality Education...
setlocal
set ROOT=%~dp0
java -cp "%ROOT%bin" main.Main
endlocal
pause
