export SCANFOLDRUNDIR=${PWD}
export SCANFOLDPYTHONINTERPRETER=$(which python)
export SCANFOLDRUNSCRIPT=${SCANFOLDRUNDIR}/scanfold/run_scanfold.py
export SCANFOLDISBUNDLED=TRUE
export SCANFOLDMPUSETHREADS=TRUE
xattr -r -d com.apple.quarantine .
open IGV.app