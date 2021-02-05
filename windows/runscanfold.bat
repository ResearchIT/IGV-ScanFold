setlocal enableDelayedExpansion

set "options=-c:"" -g: -i:"" -j:"" -n:"" -r:"" -s:"" -t:"" -w:"" -j:"" -y:"" -z:"" -d:"" -o:"""

:: https://stackoverflow.com/questions/1971824/windows-bat-file-optional-argument-parsing
for %%O in (%options%) do for /f "tokens=1,* delims=:" %%A in ("%%O") do set "%%A=%%~B"
:loop
if not "%~1"=="" (
  set "test=!options:*%~1:=! "
  if "!test!"=="!options! " (
      echo Error: Invalid option %~1
  ) else if "!test:~0,1!"==" " (
      set "%~1=1"
  ) else (
      set "%~1=%~2"
      shift /1
  )
  shift /1
  goto :loop
)

:: To get the value of a single parameter, just remember to include the `-`
::echo The value of -username is: !-username!

SET COMPETITION=!-c!
SET INPUTFILE=!-i!
SET SEQUENCENAME=!-n!
SET RANDOMIZATIONS=!-r!
SET STEPSIZE=!-s!
SET TEMPERATURE=!-t!
SET WINDOWSIZE=!-w!
SET RANDOMIZATIONTYPE=!-y!
SET STARTPOS=!-z!
SET STRAND=!-d!
SET WORKDIR=!-o!

if not "!-g!"=="" (
	SET GLOBALREFOLD=--global_refold
) else (
	SET GLOBALREFOLD=
)

if not "!-j!"=="" (
	SET JARLOCATION=!-j!
	for %%F in (%JARLOCATION%) do SET JARDIR=%%~dpF
)

copy %INPUTFILE% %WORKDIR%\input.fasta
SET INPUTFILE=%WORKDIR%\input.fasta

::Get the current batch file's short path
for %%x in (%0) do set BatchPath=%%~dpsx
for %%x in (%BatchPath%) do set BatchPath=%%~dpsx

SET BASEPATH=%BatchPath%\..

SET PATH=%PATH%;%BASEPATH%\ViennaRNA;%BASEPATH%\RNAstructure
SET DATAPATH=%BASEPATH%\RNAstructure\data_tables

cd %WORKDIR%

SET SCANOUTPATH=%WORKDIR%\outputXXX.scan-out.tsv
SET ZSCOREWIGFILEPATH=%WORKDIR%\outputXXX.zscore.wig
SET MFEWIGFILEPATH=%WORKDIR%\outputXXX.mfe.wig
SET EDWIGFILEPATH=%WORKDIR%\outputXXX.ed.wig
SET PVALUEWIGFILEPATH=%WORKDIR%\outputXXX.pvalue.wig
SET FASTAFILEPATH=%WORKDIR%\inputXXX.fasta
SET FASTAINDEX=%WORKDIR%\inputXXX.fasta.fai

%BASEPATH%\scanfold\python-3.8.6-embed-amd64\python -u %BASEPATH%\scanfold\ScanFold-Scan_IGV.py ^
    -i %INPUTFILE% ^
    -r %RANDOMIZATIONS% ^
    -s %STEPSIZE% ^
    -t %TEMPERATURE% ^
    -w %WINDOWSIZE% ^
    -d %STRAND% ^
    --start %STARTPOS% ^
    --name "%SEQUENCENAME%" ^
    -type %RANDOMIZATIONTYPE% ^
    --scan_out_path %SCANOUTPATH% ^
    --zscore_wig_file_path %ZSCOREWIGFILEPATH% ^
    --mfe_wig_file_path %MFEWIGFILEPATH% ^
    --ed_wig_file_path %EDWIGFILEPATH% ^
    --pvalue_wig_file_path %PVALUEWIGFILEPATH% ^
    --fasta_file_path %FASTAFILEPATH% ^
    --fasta_index %FASTAINDEX%

SET OUT1=%WORKDIR%\outputXXX.nofilter.ct
SET OUT2=%WORKDIR%\outputXXX.-1filter.ct
SET OUT3=%WORKDIR%\outputXXX.-2filter.ct
SET LOGFILE=%WORKDIR%\outputXXX.log.txt
SET FINALPARTNERS=%WORKDIR%\outputXXX.final_partners.txt
SET BPTRACK=%WORKDIR%\outputXXX.IGV.bp
SET FASTATRACK=%WORKDIR%\outputXXX.input.fa
SET DBNFILEPATH=%WORKDIR%\outputXXX.-2filter.scanfoldvarna
SET DBNFILEPATH1=%WORKDIR%\outputXXX.dbnfile1.dbn
SET DBNFILEPATH2=%WORKDIR%\outputXXX.dbnfile2.dbn
SET DBNFILEPATH3=%WORKDIR%\outputXXX.dbnfile3.dbn
SET DBNFILEPATH4=%WORKDIR%\outputXXX.DBNstructures.txt
SET STRUCTUREEXTRACTFILE=%WORKDIR%\outputXXX.ExtractedStructures.txt
SET FINALPARTNERSWIG=%WORKDIR%\outputXXX.final_partners_zscore.wig
SET FASTAINDEX=%WORKDIR%\outputXXX.fai

%BASEPATH%\scanfold\python-3.8.6-embed-amd64\python -u %BASEPATH%\scanfold\ScanFold-Fold_IGV.py ^
    -i %SCANOUTPATH% ^
    --name "%SEQUENCENAME%" ^
    -c %COMPETITION% ^
    -d %STRAND% ^
    --out1 %OUT1% ^
    --out2 %OUT2% ^
    --out3 %OUT3% ^
    --out4 %LOGFILE% ^
    --out5 %FINALPARTNERS% ^
    --out6 %BPTRACK% ^
    --out7 %FASTATRACK% ^
    --dbn_file_path %DBNFILEPATH% ^
    --dbn_file_path1 %DBNFILEPATH1% ^
    --dbn_file_path2 %DBNFILEPATH2% ^
    --dbn_file_path3 %DBNFILEPATH3% ^
    --dbn_file_path4 %DBNFILEPATH4% ^
    --final_partners_wig %FINALPARTNERSWIG% ^
    --structure_extract_file %STRUCTUREEXTRACTFILE% ^
    --fasta_index %FASTAINDEX% ^
    %GLOBALREFOLD%
	
echo load %BPTRACK% >> %WORKDIR%\batchfile.txt
echo load %FINALPARTNERSWIG% >> %WORKDIR%\batchfile.txt
echo load %MFEWIGFILEPATH% >> %WORKDIR%\batchfile.txt
echo load %ZSCOREWIGFILEPATH% >> %WORKDIR%\batchfile.txt
echo load %PVALUEWIGFILEPATH% >> %WORKDIR%\batchfile.txt
echo load %EDWIGFILEPATH% >> %WORKDIR%\batchfile.txt
echo load %DBNFILEPATH% >> %WORKDIR%\batchfile.txt

@echo off

if defined JARLOCATION (
  cd %BASEPATH%\IGV
  set JAVA_HOME=%BASEPATH%\IGV\jdk-11
  set JAVA_CMD=%BASEPATH%\IGV\jdk-11\bin\javaw
  start %JAVA_CMD% ^
     -showversion ^
    --module-path=lib ^
	-Xmx4g ^
	-Dproduction=true ^
	@igv.args ^
	-Djava.net.preferIPv4Stack=true ^
	-Dsun.java2d.noddraw=true ^
	--module=org.igv/org.broad.igv.ui.Main ^
	--genome=%FASTATRACK% ^
	--igvDirectory=%WORKDIR%\igv ^
	--batch=%WORKDIR%\batchfile.txt   
) else (
  echo BATCHFILEFIRSTSENTINEL%WORKDIR%\batchfile.txtBATCHFILESECONDSENTINEL
)