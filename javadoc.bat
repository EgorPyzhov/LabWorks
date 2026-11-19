@echo off
cd /d "%~dp0"
javadoc -d docs -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -private -cp "lib/*" -sourcepath src -subpackages labworks
pause
