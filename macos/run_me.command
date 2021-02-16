export SCANFOLDRUNDIR=$(cd `dirname $(readlink $0 || echo $0)` && pwd)
export SCANFOLDPYTHONINTERPRETER=$(which python)
export SCANFOLDRUNSCRIPT=${SCANFOLDRUNDIR}/scanfold/run_scanfold.py
export SCANFOLDISBUNDLED=TRUE
#switch to this if fork doesn't work
#export SCANFOLDMPUSETHREADS=TRUE
export SCANFOLDMPMETHOD=fork
cd "${SCANFOLDRUNDIR}"
xattr -r -d com.apple.quarantine . >/dev/null 2>&1
open IGV.app