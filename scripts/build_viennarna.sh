. env/bin/activate
VIENNA_PREFIX=${PWD}/env
pushd lib
curl -O https://www.tbi.univie.ac.at/RNA/download/sourcecode/2_4_x/ViennaRNA-2.4.14.tar.gz
tar -xzvf ViennaRNA-2.4.14.tar.gz
pushd ViennaRNA-2.4.14
./configure --prefix=${VIENNA_PREFIX} --without-perl --without-python
make install
popd
popd