. env/bin/activate

while getopts ":c:gi:j:n:r:s:t:w:y:z:" opt; do
  case ${opt} in
    c )
      COMPETITION=$OPTARG
      ;;
    g )
      GLOBALREFOLD="--global_refold"
      ;;
    i )
      INPUTFILE=$OPTARG
      ;;
    j )
      # If this is passed, spawn a new IGV process instead of loading it in the existing one
      JARLOCATION=$OPTARG
      JARDIR=`dirname $(readlink $JARLOCATION || echo $JARLOCATION)`
      ;;
    n )
      SEQUENCENAME=$OPTARG
      ;;
    r )
      RANDOMIZATIONS=$OPTARG
      ;;
    s )
      STEPSIZE=$OPTARG
      ;;
    t )
      TEMPERATURE=$OPTARG
      ;;
    w )
      WINDOWSIZE=$OPTARG
      ;;
    y )
      RANDOMIZATIONTYPE=$OPTARG
      ;;
    z )
      STARTPOS=$OPTARG
      ;;
    \? )
      echo "Invalid option: $OPTARG" 1>&2
      ;;
    : )
      echo "Invalid option: $OPTARG requires an argument" 1>&2
      ;;
  esac
done
shift $((OPTIND -1))

export DATAPATH=${PWD}/env/data_tables

INPUTFNAME=$(basename ${INPUTFILE})

SCRIPTDIR=${PWD}/ScanFold/
WORKDIR=$(mktemp -d)
cp ${INPUTFILE} ${WORKDIR}/

pushd ${WORKDIR}

SCANOUTPATH=$(mktemp ${WORKDIR}/outputXXX.scan-out.tsv)
ZSCOREWIGFILEPATH=$(mktemp ${WORKDIR}/outputXXX.zscore.wig)
MFEWIGFILEPATH=$(mktemp ${WORKDIR}/outputXXX.mfe.wig)
EDWIGFILEPATH=$(mktemp ${WORKDIR}/outputXXX.ed.wig)
PVALUEWIGFILEPATH=$(mktemp ${WORKDIR}/outputXXX.pvalue.wig)
FASTAFILEPATH=$(mktemp ${WORKDIR}/inputXXX.fasta)
FASTAINDEX=$(mktemp ${WORKDIR}/inputXXX.fasta.fai)

python ${SCRIPTDIR}/ScanFold-Scan_IGV.py \
    -i ${INPUTFILE} \
    -r ${RANDOMIZATIONS} \
    -s ${STEPSIZE} \
    -t ${TEMPERATURE} \
    -w ${WINDOWSIZE} \
    --start ${STARTPOS} \
    --name "${SEQUENCENAME}" \
    -type ${RANDOMIZATIONTYPE} \
    --scan_out_path ${SCANOUTPATH} \
    --zscore_wig_file_path ${ZSCOREWIGFILEPATH} \
    --mfe_wig_file_path ${MFEWIGFILEPATH} \
    --ed_wig_file_path ${EDWIGFILEPATH} \
    --pvalue_wig_file_path ${PVALUEWIGFILEPATH} \
    --fasta_file_path ${FASTAFILEPATH} \
    --fasta_index ${FASTAINDEX}

OUT1=$(mktemp ${WORKDIR}/outputXXX.nofilter.ct)
OUT2=$(mktemp ${WORKDIR}/outputXXX.-1filter.ct)
OUT3=$(mktemp ${WORKDIR}/outputXXX.-2filter.ct)
LOGFILE=$(mktemp ${WORKDIR}/outputXXX.log.txt)
FINALPARTNERS=$(mktemp ${WORKDIR}/outputXXX.final_partners.txt)
BPTRACK=$(mktemp ${WORKDIR}/outputXXX.IGV.bp)
FASTATRACK=$(mktemp ${WORKDIR}/outputXXX.input.fa)
#DBNFILEPATH=$(mktemp ${WORKDIR}/outputXXX.-2filter.dbn)
DBNFILEPATH=$(mktemp ${WORKDIR}/outputXXX.-2filter.scanfoldvarna)
DBNFILEPATH1=$(mktemp ${WORKDIR}/outputXXX.dbnfile1.dbn)
DBNFILEPATH2=$(mktemp ${WORKDIR}/outputXXX.dbnfile2.dbn)
DBNFILEPATH3=$(mktemp ${WORKDIR}/outputXXX.dbnfile3.dbn)
DBNFILEPATH4=$(mktemp ${WORKDIR}/outputXXX.DBNstructures.txt)
STRUCTUREEXTRACTFILE=$(mktemp ${WORKDIR}/outputXXX.ExtractedStructures.txt)
FINALPARTNERSWIG=$(mktemp ${WORKDIR}/outputXXX.final_partners_zscore.wig)
FASTAINDEX=$(mktemp ${WORKDIR}/outputXXX.fai)

python ${SCRIPTDIR}/ScanFold-Fold_IGV.py \
    -i ${SCANOUTPATH} \
    --name "${SEQUENCENAME}" \
    -c ${COMPETITION} \
    --out1 ${OUT1} \
    --out2 ${OUT2} \
    --out3 ${OUT3} \
    --out4 ${LOGFILE} \
    --out5 ${FINALPARTNERS} \
    --out6 ${BPTRACK} \
    --out7 ${FASTATRACK} \
    --dbn_file_path ${DBNFILEPATH} \
    --dbn_file_path1 ${DBNFILEPATH1} \
    --dbn_file_path2 ${DBNFILEPATH2} \
    --dbn_file_path3 ${DBNFILEPATH3} \
    --dbn_file_path4 ${DBNFILEPATH4} \
    --final_partners_wig ${FINALPARTNERSWIG} \
    --structure_extract_file ${STRUCTUREEXTRACTFILE} \
    --fasta_index ${FASTAINDEX} \
    ${GLOBALREFOLD}

popd

echo "load ${BPTRACK}" >> ${WORKDIR}/batchfile.txt
echo "load ${FINALPARTNERSWIG}" >> ${WORKDIR}/batchfile.txt
echo "load ${MFEWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${ZSCOREWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${PVALUEWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${EDWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${DBNFILEPATH}" >> ${WORKDIR}/batchfile.txt

if [ -n "${JARLOCATION}" ]; then
  java -showversion --module-path="${JARDIR}" -Xmx4g \
      -Dapple.laf.useScreenMenuBar=true \
      -Djava.net.preferIPv4Stack=true \
      --module=org.igv/org.broad.igv.ui.Main \
      --genome=${FASTATRACK} \
      --igvDirectory=${WORKDIR}/igv \
      --batch=${WORKDIR}/batchfile.txt &
else
  echo BATCHFILEFIRSTSENTINEL${WORKDIR}/batchfile.txtBATCHFILESECONDSENTINEL
fi;