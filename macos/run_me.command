#!/bin/bash

export SCANFOLDRUNDIR=$(cd `dirname $(readlink $0 || echo $0)` && cd .. && pwd)
export SCANFOLDPYTHONINTERPRETER=$(which python3)
export SCANFOLDRUNSCRIPT=${SCANFOLDRUNDIR}/scanfold/run_scanfold.py
export SCANFOLDISBUNDLED=TRUE
#switch to this if fork doesn't work
#export SCANFOLDMPUSETHREADS=TRUE
export SCANFOLDMPMETHOD=fork
cd "${SCANFOLDRUNDIR}"
xattr -r -d com.apple.quarantine . >/dev/null 2>&1

## we don't actually launch IGV here anymore
## this file gets prepended to the IGV app's run script during bundling
