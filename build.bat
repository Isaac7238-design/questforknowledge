@echo off
echo ============================================
echo  Lucienne: Quest for Quality Education
echo  TMF2954 Java Programming - SDG 4
echo  Build Script
echo ============================================
echo.
setlocal

set ROOT=%~dp0
set SRC=%ROOT%src
set BIN=%ROOT%bin
set JAVA_BIN=%JAVA_HOME%\bin

:: Step 1: Clean build directory
echo [1/4] Cleaning build directory...
if exist "%BIN%" rmdir /s /q "%BIN%"
mkdir "%BIN%"

:: Step 2: Compile all Java source files
echo [2/4] Compiling Java source files...
"%JAVA_BIN%\javac.exe" -encoding UTF-8 -sourcepath "%SRC%" -d "%BIN%" "%SRC%\main\Main.java"
if %errorlevel% neq 0 (
    echo.
    echo *** COMPILATION FAILED ***
    pause
    exit /b 1
)
echo     Compilation successful.

:: Step 3: Copy resources
echo [3/4] Copying resources...
xcopy "%SRC%\maps" "%BIN%\maps\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\sound" "%BIN%\sound\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\player" "%BIN%\player\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\npc" "%BIN%\npc\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\monster" "%BIN%\monster\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\objects" "%BIN%\objects\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\tiles" "%BIN%\tiles\" /E /Y /Q >nul 2>&1
xcopy "%SRC%\font" "%BIN%\font\" /E /Y /Q >nul 2>&1
if exist "%SRC%\player_title.png" copy "%SRC%\player_title.png" "%BIN%\player_title.png" >nul
echo     Resources copied.

:: Step 4: Package into JAR
echo [4/4] Packaging JAR...
"%JAVA_BIN%\jar.exe" cfm "%ROOT%QuestForKnowledge.jar" "%ROOT%MANIFEST.MF" -C "%BIN%" .
if %errorlevel% neq 0 (
    echo.
    echo *** JAR PACKAGING FAILED ***
    pause
    exit /b 1
)
echo     JAR created: QuestForKnowledge.jar

echo.
echo ============================================
echo  BUILD COMPLETE!
echo ============================================
echo.
echo  To run the game:
echo    java -jar QuestForKnowledge.jar
echo    OR double-click QuestForKnowledge.jar
echo    OR run: run.bat
echo.
echo  To create a standalone EXE (no Java needed):
echo    Run: package_exe.bat
echo ============================================
endlocal
pause
