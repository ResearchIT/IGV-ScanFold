. env/bin/activate
VIENNA_PREFIX=${PWD}/env
pushd lib
curl -L -O https://github.com/ViennaRNA/ViennaRNA/releases/download/v2.4.18/ViennaRNA-2.4.18.tar.gz
tar -xzvf ViennaRNA-2.4.18.tar.gz
pushd ViennaRNA-2.4.18
./configure --prefix=${VIENNA_PREFIX} --without-perl --without-python
make install
popd
popd