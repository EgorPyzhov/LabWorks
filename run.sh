#!/bin/sh
javac -encoding UTF-8 -cp "lib/*" -d out src/labworks/*.java src/labworks/module1/*.java src/labworks/module2/*.java && java -Dfile.encoding=UTF-8 -cp "out:lib/*" labworks.Main
