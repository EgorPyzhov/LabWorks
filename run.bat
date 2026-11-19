@echo off
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
    echo JDK not found. Install JDK 17 or newer and run again.
    pause
    exit /b 1
)
if exist out rmdir /s /q out
javac -encoding UTF-8 -cp "lib/*" -d out src/labworks/*.java src/labworks/module1/*.java src/labworks/module2/*.java
if errorlevel 1 (
    echo COMPILATION ERROR - see the messages above.
    pause
    exit /b 1
)
java -Dfile.encoding=UTF-8 -cp "out;lib/*" --enable-native-access=ALL-UNNAMED labworks.Main
if errorlevel 1 pause
