TOPLEVEL=${PWD}
BUNDLE_PREFIX=${PWD}/build
rm -rf ${BUNDLE_PREFIX}
mkdir -p ${BUNDLE_PREFIX}

# build IGV and bundle with JRE
pushd lib
curl -L -O https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.9.1%2B1/OpenJDK11U-jre_x64_windows_hotspot_11.0.9.1_1.zip
unzip OpenJDK11U-jre_x64_windows_hotspot_11.0.9.1_1.zip
rm  OpenJDK11U-jre_x64_windows_hotspot_11.0.9.1_1.zip
popd

pushd igv
./gradlew clean
./gradlew createWinWithJavaDist -PjdkBundleWindows=${TOPLEVEL}/lib/jdk-11.0.9.1+1-jre
mv build/IGV-WinExe-WithJava-dist/IGV_user ${BUNDLE_PREFIX}/IGV
popd

# bundle python

mkdir -p ${BUNDLE_PREFIX}/scanfold/

pushd lib
curl -L -O https://www.python.org/ftp/python/3.8.6/python-3.8.6-embed-amd64.zip
unzip -d ${BUNDLE_PREFIX}/scanfold/python-3.8.6-embed-amd64 python-3.8.6-embed-amd64.zip
rm python-3.8.6-embed-amd64.zip
popd

pushd ${BUNDLE_PREFIX}/scanfold/python-3.8.6-embed-amd64
echo '
..
site-packages
' >> python38._pth
mkdir -p site-packages

# biopython
curl -L -O https://files.pythonhosted.org/packages/4a/93/abbc0693692afe90e9f06dfc7b581b069326f7496c31bd0b211cb9cad79e/biopython-1.78-cp38-cp38-win_amd64.whl
unzip -d site-packages biopython-1.78-cp38-cp38-win_amd64.whl
rm biopython-1.78-cp38-cp38-win_amd64.whl

# numpy
curl -L -O https://files.pythonhosted.org/packages/40/db/5060f18b0116f00ee73f8365efc9c95bd5496946290b0e7c97b6ee89dffe/numpy-1.19.4-cp38-cp38-win_amd64.whl
unzip -d site-packages numpy-1.19.4-cp38-cp38-win_amd64.whl
rm numpy-1.19.4-cp38-cp38-win_amd64.whl
popd

# scanfold
pushd ${BUNDLE_PREFIX}/scanfold
cp ${TOPLEVEL}/ScanFold/{ScanFold-Scan_IGV.py,ScanFold-Fold_IGV.py,ScanFoldSharedIGV.py} .
cp ${TOPLEVEL}/windows/runscanfold.bat .
popd

# viennarna
pushd lib
curl -L -O https://github.com/ViennaRNA/ViennaRNA/releases/download/v2.4.16/ViennaRNA-2.4.16.tar.gz
tar -xzvf ViennaRNA-2.4.16.tar.gz
pushd ViennaRNA-2.4.16
mkdir target
export ac_cv_func_realloc_0_nonnull=yes
export ac_cv_func_malloc_0_nonnull=yes
CONFIGURE_OPTIONS=" --without-swig \
                    --without-doc \
                    --without-forester \
                    --with-cluster \
                    --with-kinwalker \
                    --disable-pthreads \
                    --disable-tty-colors \
                    --disable-lto"
./configure --host=x86_64-w64-mingw32 --prefix=${PWD}/target ${CONFIGURE_OPTIONS}
make
make install
mkdir -p ${BUNDLE_PREFIX}/ViennaRNA
cp /usr/lib/gcc/x86_64-w64-mingw32/*-win32/libssp-0.dll ${BUNDLE_PREFIX}/ViennaRNA/
mv target/bin/RNAfold.exe ${BUNDLE_PREFIX}/ViennaRNA/
popd
popd

# rnastructure
pushd lib
curl -L -O  http://rna.urmc.rochester.edu/Releases/current/RNAstructureWindowsTextInterfaces64bit.zip
unzip RNAstructureWindowsTextInterfaces64bit.zip
mv RNAstructureWindowsTextInterfaces64bit.zip
mkdir ${BUNDLE_PREFIX}/RNAstructure
mv RNAstructure/exe/ct2dot.exe ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/gpl.txt ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/data_tables ${BUNDLE_PREFIX}/RNAstructure/
popd

# main
pushd ${BUNDLE_PREFIX}
cp ${TOPLEVEL}/windows/runme.bat .
popd