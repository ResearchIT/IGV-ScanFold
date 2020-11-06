setlocal

::Get the current batch file's short path
for %%x in (%0) do set BatchPath=%%~dpsx
for %%x in (%BatchPath%) do set BatchPath=%%~dpsx

SET SCANFOLDRUNDIR=%BatchPath%
SET SCANFOLDRUNSCRIPT=%SCANFOLDRUNDIR%\scanfold\runscanfold.bat

cd %BatchPath%\IGV

igv