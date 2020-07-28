@echo off

if exist "%JAVA_HOME%" (
  set "JAVA=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA=java.exe"
)

set basedir=%~f0

:Strip
set removed=%basedir:~-1%
set basedir=%basedir:~0,-1%
if NOT "%removed%"=="\" goto Strip

set CONV_HOME=%basedir%

:Launch
start "Converter UI" /B "%JAVA%" -Dfile.encoding=UTF-8 -jar "%CONV_HOME%\ConverterUI.jar"
