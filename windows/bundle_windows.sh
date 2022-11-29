TOPLEVEL=${PWD}
BUNDLE_PREFIX=${PWD}/build
rm -rf ${BUNDLE_PREFIX}
mkdir -p ${BUNDLE_PREFIX}

# build IGV and bundle with JRE
pushd lib
curl -L -O https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.17%2B8/OpenJDK11U-jre_x64_windows_hotspot_11.0.17_8.zip
unzip OpenJDK11U-jre_x64_windows_hotspot_11.0.17_8.zip
rm  OpenJDK11U-jre_x64_windows_hotspot_11.0.17_8.zip
popd

pushd igv
./gradlew clean
./gradlew createWinWithJavaDist -PjdkBundleWindows=${TOPLEVEL}/lib/jdk-11.0.17+8-jre
mv build/IGV-WinExe-WithJava-dist/IGV_user ${BUNDLE_PREFIX}/IGV
popd

# bundle python

mkdir -p ${BUNDLE_PREFIX}/scanfold/

pushd lib
curl -L -O https://www.python.org/ftp/python/3.10.8/python-3.10.8-embed-amd64.zip
unzip -d ${BUNDLE_PREFIX}/scanfold/python-3.10.8-embed-amd64 python-3.10.8-embed-amd64.zip
rm python-3.10.8-embed-amd64.zip
popd

pushd ${BUNDLE_PREFIX}/scanfold/python-3.10.8-embed-amd64
echo '
python310.zip
.
..
site-packages
' >> python310._pth
mkdir -p site-packages

# biopython
curl -L -O https://files.pythonhosted.org/packages/06/ed/6fbab8be0b4868f5105d76f3ab8b202c1bffd23d43cd422ba485af374b71/biopython-1.80-cp310-cp310-win_amd64.whl
unzip -d site-packages biopython-1.80-cp310-cp310-win_amd64.whl
rm biopython-1.80-cp310-cp310-win_amd64.whl

# numpy
curl -L -O https://files.pythonhosted.org/packages/6a/03/ae6c3c307f9c5c7516de3df3e764ebb1de33e54e197f0370992138433ef4/numpy-1.23.5-cp310-cp310-win_amd64.whl
unzip -d site-packages numpy-1.23.5-cp310-cp310-win_amd64.whl
rm numpy-1.23.5-cp310-cp310-win_amd64.whl
popd

# scanfold
pushd ${BUNDLE_PREFIX}/scanfold
cp ${TOPLEVEL}/ScanFold/{ScanFold-Scan_IGV.py,ScanFold-Fold_IGV.py,ScanFoldSharedIGV.py} .
cp ${TOPLEVEL}/scripts/run_scanfold.py .
popd

# viennarna
pushd lib
curl -L -O https://github.com/ViennaRNA/ViennaRNA/releases/download/v2.4.18/ViennaRNA-2.4.18.tar.gz
tar -xzvf ViennaRNA-2.4.18.tar.gz
pushd ViennaRNA-2.4.18
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
cp /usr/lib/gcc/x86_64-w64-mingw32/*-win32/libgcc_s_seh-1.dll ${BUNDLE_PREFIX}/ViennaRNA/
cp /usr/lib/gcc/x86_64-w64-mingw32/*-win32/libstdc++-6.dll ${BUNDLE_PREFIX}/ViennaRNA/
cp /usr/x86_64-w64-mingw32/lib/libwinpthread-1.dll ${BUNDLE_PREFIX}/ViennaRNA/
cp /usr/lib/gcc/x86_64-w64-mingw32/*-win32/libgomp-1.dll ${BUNDLE_PREFIX}/ViennaRNA/
mv target/bin/RNAfold.exe ${BUNDLE_PREFIX}/ViennaRNA/
cp license.txt ${BUNDLE_PREFIX}/ViennaRNA/
popd
popd

# rnastructure
pushd lib
curl -L -O  http://rna.urmc.rochester.edu/Releases/current/RNAstructureWindowsTextInterfaces64bit.zip
unzip RNAstructureWindowsTextInterfaces64bit.zip
mv RNAstructureWindowsTextInterfaces64bit.zip
mkdir ${BUNDLE_PREFIX}/RNAstructure
mv RNAstructure/exe/ct2dot.exe ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/exe/Fold.exe ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/gpl.txt ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/data_tables ${BUNDLE_PREFIX}/RNAstructure/
popd

# main
pushd ${BUNDLE_PREFIX}
cp ${TOPLEVEL}/windows/IGV-ScanFold.bat .
zip -r ${TOPLEVEL}/IGV-ScanFold-windows.zip *
popd
