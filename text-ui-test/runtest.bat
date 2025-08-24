@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM CLEAR DATA FILE BEFORE TESTING
if exist data\pingpong.txt del data\pingpong.txt
if exist data rmdir data /S /Q

REM compile the code into the bin folder
javac -cp ..\src\main\java -Xlint:none -d ..\bin ..\src\main\java\pingpong\*.java ..\src\main\java\pingpong\task\*.java ..\src\main\java\pingpong\command\*.java ..\src\main\java\pingpong\ui\*.java ..\src\main\java\pingpong\storage\*.java
IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -classpath ..\bin pingpong.Pingpong < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT