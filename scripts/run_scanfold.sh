. env/bin/activate
INPUTFILE=$1
INPUTFNAME=$(basename ${INPUTFILE})
WORKDIR=$(mktemp -d)
shift
shift
COMPETITION=$1
shift
cp ${INPUTFILE} ${WORKDIR}/
python ScanFold/ScanFold-Scan.py -i ${WORKDIR}/${INPUTFNAME} "$@"
python ScanFold/ScanFold-Fold.py -i ${WORKDIR}/${INPUTFNAME}*.txt -c ${COMPETITION}
find ${WORKDIR} -type f -name '*.dp' -exec echo 'load {}' \; > ${WORKDIR}/batchfile.txt
echo BATCHFILEFIRSTSENTINEL${WORKDIR}/batchfile.txtBATCHFILESECONDSENTINEL