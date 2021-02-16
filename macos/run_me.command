export SCANFOLDRUNDIR=`dirname $(readlink $0 || echo $0)`
export SCANFOLDPYTHONINTERPRETER=$(which python)
export SCANFOLDRUNSCRIPT=${SCANFOLDRUNDIR}/scanfold/run_scanfold.py
export SCANFOLDISBUNDLED=TRUE
export SCANFOLDMPUSETHREADS=TRUE
cd "${SCANFOLDRUNDIR}"
xattr -r -d com.apple.quarantine . >/dev/null 2>&1
open IGV.app