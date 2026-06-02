@echo off
echo ==============================
echo  Lucienne: Quest for Quality Education
echo  Compiling...
echo ==============================
setlocal
set ROOT=%~dp0
set SRC=%ROOT%src
set BIN=%ROOT%bin
if not exist "%BIN%" mkdir "%BIN%"
javac -encoding UTF-8 -sourcepath "%SRC%" -d "%BIN%" "%SRC%\main\Main.java"
if %errorlevel% == 0 (
    echo.
    echo *** Compilation SUCCESSFUL ***
    echo.
    echo Copying resources...
    xcopy "%SRC%\maps" "%BIN%\maps\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\sound" "%BIN%\sound\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\player" "%BIN%\player\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\npc" "%BIN%\npc\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\monster" "%BIN%\monster\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\objects" "%BIN%\objects\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\tiles" "%BIN%\tiles\" /E /Y /Q >nul 2>&1
    xcopy "%SRC%\font" "%BIN%\font\" /E /Y /Q >nul 2>&1
    echo Resources copied.
    echo Run with: run.bat
) else (
    echo.
    echo *** Compilation FAILED ***
)
endlocal
pause
