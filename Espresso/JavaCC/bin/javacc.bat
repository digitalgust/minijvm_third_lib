set BATPATH=%~dp0
rem echo %BATPATH%
java -cp %BATPATH%\..\JavaCC.zip COM.sun.labs.javacc.Main  %1 