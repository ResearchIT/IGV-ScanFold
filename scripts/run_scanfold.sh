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

WORKDIR=$(mktemp -d)
cp ${INPUTFILE} ${WORKDIR}/

SCANOUTPATH=$(mktemp -p ${WORKDIR} outputXXX.scan-out.tsv)
ZSCOREWIGFILEPATH=$(mktemp -p ${WORKDIR} outputXXX.zscore.wig)
MFEWIGFILEPATH=$(mktemp -p ${WORKDIR} outputXXX.mfe.wig)
EDWIGFILEPATH=$(mktemp -p ${WORKDIR} outputXXX.ed.wig)
PVALUEWIGFILEPATH=$(mktemp -p ${WORKDIR} outputXXX.pvalue.wig)
FASTAFILEPATH=$(mktemp -p ${WORKDIR} inputXXX.fasta)
FASTAINDEX=$(mktemp -p ${WORKDIR} inputXXX.fasta.fai)

python ScanFold/ScanFold-Scan_IGV.py \
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

OUT1=$(mktemp -p ${WORKDIR} outputXXX.nofilter.ct)
OUT2=$(mktemp -p ${WORKDIR} outputXXX.-1filter.ct)
OUT3=$(mktemp -p ${WORKDIR} outputXXX.-2filter.ct)
LOGFILE=$(mktemp -p ${WORKDIR} outputXXX.log.txt)
FINALPARTNERS=$(mktemp -p ${WORKDIR} outputXXX.final_partners.txt)
BPTRACK=$(mktemp -p ${WORKDIR} outputXXX.IGV.bp)
FASTATRACK=$(mktemp -p ${WORKDIR} outputXXX.input.fa)
DBNFILEPATH=$(mktemp -p ${WORKDIR} outputXXX.-2filter.dbn)
DBNFILEPATH1=$(mktemp -p ${WORKDIR} outputXXX.dbnfile1.dbn)
DBNFILEPATH2=$(mktemp -p ${WORKDIR} outputXXX.dbnfile2.dbn)
DBNFILEPATH3=$(mktemp -p ${WORKDIR} outputXXX.dbnfile3.dbn)
DBNFILEPATH4=$(mktemp -p ${WORKDIR} outputXXX.DBNstructures.txt)
STRUCTUREEXTRACTFILE=$(mktemp -p ${WORKDIR} outputXXX.ExtractedStructures.txt)
FINALPARTNERSWIG=$(mktemp -p ${WORKDIR} outputXXX.final_partners_zscore.wig)
FASTAINDEX=$(mktemp -p ${WORKDIR} outputXXX.fai)

python ScanFold/ScanFold-Fold_IGV.py \
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

echo "load ${BPTRACK}" >> ${WORKDIR}/batchfile.txt
echo "load ${FINALPARTNERSWIG}" >> ${WORKDIR}/batchfile.txt
echo "load ${MFEWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${ZSCOREWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${PVALUEWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt
echo "load ${EDWIGFILEPATH}" >> ${WORKDIR}/batchfile.txt

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