setlocal

::Get the current batch file's short path
for %%x in (%0) do set BatchPath=%%~dpsx
for %%x in (%BatchPath%) do set BatchPath=%%~dpsx

SET SCANFOLDRUNDIR=%BatchPath%
SET SCANFOLDPYTHONINTERPRETER=%SCANFOLDRUNDIR%\scanfold\python-3.8.6-embed-amd64\python
SET SCANFOLDRUNSCRIPT=%SCANFOLDRUNDIR%\scanfold\run_scanfold.py

cd %BatchPath%\IGV

igv