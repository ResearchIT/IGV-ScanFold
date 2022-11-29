setlocal

SET SCANFOLDRUNDIR=%cd%
SET SCANFOLDPYTHONINTERPRETER=%SCANFOLDRUNDIR%\scanfold\python-3.10.8-embed-amd64\python
SET SCANFOLDRUNSCRIPT=%SCANFOLDRUNDIR%\scanfold\run_scanfold.py

cd %cd%\IGV

igv-launcher
