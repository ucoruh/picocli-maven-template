@echo off
@setlocal enableextensions
@cd /d "%~dp0"

echo Running Application
java -jar picocli-maven-template/target/picocli-maven-template-1.0-SNAPSHOT.jar

echo Operation Completed!
pause