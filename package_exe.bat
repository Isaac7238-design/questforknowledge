@echo off
echo ============================================
echo  Lucienne: Quest for Quality Education
echo  Creating Standalone EXE (Windows)
echo ============================================
echo.
setlocal

set ROOT=%~dp0
set JAVA_BIN=%JAVA_HOME%\bin

:: Check if JAR exists
if not exist "%ROOT%QuestForKnowledge.jar" (
    echo ERROR: QuestForKnowledge.jar not found!
    echo Please run build.bat first.
    pause
    exit /b 1
)

:: Clean previous dist
if exist "%ROOT%dist" rmdir /s /q "%ROOT%dist"

echo Creating native application image...
echo This bundles a Java runtime so users don't need Java installed.
echo.

"%JAVA_BIN%\jpackage.exe" ^
    --type app-image ^
    --input "%ROOT%" ^
    --main-jar QuestForKnowledge.jar ^
    --name "QuestForKnowledge" ^
    --dest "%ROOT%dist" ^
    --java-options "-Dfile.encoding=UTF-8" ^
    --app-version "1.0.0" ^
    --description "Lucienne: Quest for Quality Education - SDG 4 RPG Game" ^
    --vendor "TMF2954 Group Project"

if %errorlevel% neq 0 (
    echo.
    echo *** EXE PACKAGING FAILED ***
    echo Make sure JAVA_HOME is set to JDK 14+ (jpackage required)
    pause
    exit /b 1
)

echo.
echo ============================================
echo  EXE CREATED SUCCESSFULLY!
echo ============================================
echo.
echo  Location: dist\QuestForKnowledge\QuestForKnowledge.exe
echo.
echo  To distribute: zip the entire dist\QuestForKnowledge folder.
echo  Users can run QuestForKnowledge.exe directly - no Java needed!
echo ============================================
endlocal
pause
