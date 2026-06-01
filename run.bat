@echo off
setlocal enabledelayedexpansion

for %%i in ("%~dp0.") do set SHORT=%%~si\
if "%SHORT%"=="\" set SHORT=
set JAR=%SHORT%build\libs\product-manager-0.0.1-SNAPSHOT.jar

if not exist "%JAR%" (
    echo Building JAR...
    call "%SHORT%gradlew.bat" bootJar -x test
)

set SPRING_PROFILES_ACTIVE=dev
echo Running Product Manager on http://localhost:8080
java -jar "%JAR%"
