setlocal

SET SCANFOLDRUNDIR=%cd%
SET SCANFOLDPYTHONINTERPRETER=%SCANFOLDRUNDIR%\scanfold\python-3.8.6-embed-amd64\python
SET SCANFOLDRUNSCRIPT=%SCANFOLDRUNDIR%\scanfold\run_scanfold.py

cd %cd%\IGV

igv-launcher