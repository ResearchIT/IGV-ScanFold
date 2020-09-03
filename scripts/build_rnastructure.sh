. env/bin/activate
RNASTRUCTURE_PREFIX=${PWD}/env
pushd lib
curl -O http://rna.urmc.rochester.edu/Releases/current/RNAstructureSource.tgz
tar xzvf RNAstructureSource.tgz
pushd RNAstructure
make ct2dot
#ln -s ${PWD}/exe/ct2dot ${RNASTRUCTURE_PREFIX}/bin/ct2dot
mv ${PWD}/exe/ct2dot ${RNASTRUCTURE_PREFIX}/bin
mv ${PWD}/data_tables ${RNASTRUCTURE_PREFIX}/data_tables
popd
popd