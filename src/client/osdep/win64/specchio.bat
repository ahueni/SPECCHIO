@ECHO OFF
SET SPECCHIO_HOME=%PROGRAMFILES%\SPECCHIO
cd %SPECCHIO_HOME%
java -jar -Dncsa.hdf.hdf5lib.H5.hdf5lib=%SPECCHIO_HOME%\win64\jhdf5.dll specchio-client.jar
