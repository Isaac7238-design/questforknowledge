@echo off
echo Starting Lucienne: Quest for Quality Education...
setlocal
set ROOT=%~dp0

:: Try JAR first (preferred)
if exist "%ROOT%QuestForKnowledge.jar" (
    java -jar "%ROOT%QuestForKnowledge.jar"
) else if exist "%ROOT%bin\main\Main.class" (
    java -cp "%ROOT%bin" main.Main
) else (
    echo ERROR: Game not compiled! Run build.bat first.
)
endlocal
pause
