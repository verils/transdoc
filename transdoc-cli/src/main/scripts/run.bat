@echo off

set HOME=%~dp0
cd /d %HOME%

set CLASSPATH=%HOME%bin

set MAIN_CLASS=com.transdoc.cli.TransdocAppication

setlocal enabledelayedexpansion

for %%j in (%HOME%libs\*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%j
)

set CMD_ARGS=

:getParameter
set PARAMETER=%1
if not defined PARAMETER goto runJava
set CMD_ARGS=%CMD_ARGS% %PARAMETER%
shift /0
goto getParameter

:runJava
java -classpath %CLASSPATH% %MAIN_CLASS% %CMD_ARGS%

endlocal
echo;
pause