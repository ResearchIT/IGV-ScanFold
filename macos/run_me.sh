export SCANFOLDRUNDIR=${PWD}
export SCANFOLDPYTHONINTERPRETER=$(which python)
export SCANFOLDRUNSCRIPT=${SCANFOLDRUNDIR}/scripts/run_scanfold.py
export SCANFOLDISBUNDLED=TRUE
xattr -r -d com.apple.quarantine .
open IGV.app