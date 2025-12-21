@echo off
echo Compiling Crypto Dashboard...
mkdir bin 2>nul
mkdir data 2>nul
mkdir lib 2>nul

echo Downloading required libraries...
echo Note: Please download jfreechart-1.5.3.jar and jcommon-1.5.3.jar
echo from https://www.jfree.org/jfreechart/ and place in lib/ folder

if not exist "lib\jfreechart-1.5.3.jar" (
    echo Error: jfreechart-1.5.3.jar not found in lib folder!
    echo Please download it first.
    pause
    exit /b 1
)

if not exist "lib\jcommon-1.5.3.jar" (
    echo Error: jcommon-1.5.3.jar not found in lib folder!
    echo Please download it first.
    pause
    exit /b 1
)

echo Compiling Java files...
javac -cp "lib/*" -d bin src\**\*.java src\*\**\*.java

if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running Crypto Dashboard...
java -cp "bin;lib/*" Main

pause