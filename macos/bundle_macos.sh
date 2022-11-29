TOPLEVEL=${PWD}
BUNDLE_PREFIX=${PWD}/build
rm -rf ${BUNDLE_PREFIX}
mkdir -p ${BUNDLE_PREFIX}

# build IGV and bundle with JRE
pushd lib
curl -L -O https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.17%2B8/OpenJDK11U-jre_x64_mac_hotspot_11.0.17_8.tar.gz
tar -xzf OpenJDK11U-jre_x64_mac_hotspot_11.0.17_8.tar.gz
rm  OpenJDK11U-jre_x64_mac_hotspot_11.0.17_8.tar.gz
popd

pushd igv
./gradlew clean
./gradlew createMacAppWithJavaDistZip -PjdkBundleMac=${TOPLEVEL}/lib/jdk-11.0.17+8-jre/Contents/Home
unzip build/distributions/IGV_MacApp_user_WithJava.zip
mv IGV_User.app ${BUNDLE_PREFIX}/IGV-ScanFold.app
popd


# compile scanfold
pushd ScanFold
sed -i -e 's/#!\/usr\/local\/bin\/python.*//' ScanFold-Scan_IGV.py ScanFold-Fold_IGV.py
python3 -mvenv env
. env/bin/activate
pip install Nuitka biopython numpy
python -m nuitka --standalone --plugin-enable=numpy ScanFold-Scan_IGV.py
python -m nuitka --standalone --plugin-enable=numpy ScanFold-Fold_IGV.py
deactivate
popd

# bundle scanfold
mkdir -p ${BUNDLE_PREFIX}/scanfold/

pushd ${BUNDLE_PREFIX}/scanfold
cp -R ${TOPLEVEL}/ScanFold/{ScanFold-Scan_IGV.dist,ScanFold-Fold_IGV.dist} .
cp ${TOPLEVEL}/scripts/run_scanfold.py .
popd

# viennarna
pushd lib
curl -L -O https://github.com/ViennaRNA/ViennaRNA/releases/download/v2.4.18/ViennaRNA-2.4.18.tar.gz
tar -xzf ViennaRNA-2.4.18.tar.gz
pushd ViennaRNA-2.4.18
mkdir target
#export ac_cv_func_realloc_0_nonnull=yes
#export ac_cv_func_malloc_0_nonnull=yes
CONFIGURE_OPTIONS=" --without-doc \
                    --without-python \
                    --without-python3 \
                    --without-check \
                    --without-cluster \
                    --without-forester \
                    --without-json \
                    --without-svm \
                    --disable-unittests \
                    --without-kinwalker \
                    --without-tutorial \
                    --without-tutorial-pdf  \
                    --without-rnalocmin \
                    --without-gsl \
                    --without-swig \
                    --without-perl \
                    --disable-mpfr \
                    --disable-pthreads \
                    --disable-tty-colors \
                    --disable-lto"
./configure --prefix=${PWD}/target ${CONFIGURE_OPTIONS}
make
make install
mkdir -p ${BUNDLE_PREFIX}/ViennaRNA
mv target/bin/RNAfold ${BUNDLE_PREFIX}/ViennaRNA/
cp license.txt ${BUNDLE_PREFIX}/ViennaRNA/
popd
popd

# rnastructure
pushd lib
curl -L -O  http://rna.urmc.rochester.edu/Releases/current/RNAstructureTextInterfacesMac.tgz
tar -xzf RNAstructureTextInterfacesMac.tgz
mv RNAstructureTextInterfacesMac.tgz
mkdir ${BUNDLE_PREFIX}/RNAstructure
mv RNAstructure/exe/ct2dot ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/exe/Fold ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/gpl.txt ${BUNDLE_PREFIX}/RNAstructure/
mv RNAstructure/data_tables ${BUNDLE_PREFIX}/RNAstructure/
popd

# main
pushd ${BUNDLE_PREFIX}

# prepend our environment parameters to IGV's run script
echo '' >> ${TOPLEVEL}/macos/run_me.command
cat ${TOPLEVEL}/igv/scripts/mac.app/Contents/MacOS/IGV.sh >> ${TOPLEVEL}/macos/run_me.command
chmod +x run_me.command
mv ${TOPLEVEL}/macos/run_me.command IGV-ScanFold.app/Contents/MacOS/IGV

# bundle everything in the app
mv scanfold IGV-ScanFold.app/Contents/
mv ViennaRNA IGV-ScanFold.app/Contents/
mv RNAstructure IGV-ScanFold.app/Contents/

ls -la scanfold
ls -la ViennaRNA
ls -la RNAstructure

tar -cvf ${TOPLEVEL}/IGV-Scanfold-macos.tar .
zip -r ${TOPLEVEL}/IGV-ScanFold-macos.zip *

popd
