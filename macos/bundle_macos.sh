TOPLEVEL=${PWD}
BUNDLE_PREFIX=${PWD}/build
rm -rf ${BUNDLE_PREFIX}
mkdir -p ${BUNDLE_PREFIX}

# build IGV and bundle with JRE
pushd lib
curl -L -O https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.10%2B9/OpenJDK11U-jre_x64_mac_hotspot_11.0.10_9.tar.gz
tar -xzf OpenJDK11U-jre_x64_mac_hotspot_11.0.10_9.tar.gz
rm  OpenJDK11U-jre_x64_mac_hotspot_11.0.10_9.tar.gz
popd

pushd igv
./gradlew clean
./gradlew createMacAppWithJavaDistZip -PjdkBundleMac=${TOPLEVEL}/lib/jdk-11.0.10+9-jre/Contents/Home
unzip build/distributions/IGV_MacApp_user_WithJava.zip
mv IGV_User.app ${BUNDLE_PREFIX}/IGV.app
popd


# compile scanfold
pushd ScanFold
sed -i 's/#!\/usr\/local\/bin\/python.*//' ScanFold-Scan_IGV.py ScanFold-Fold_IGV.py
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

# main
pushd ${BUNDLE_PREFIX}
zip -r ${TOPLEVEL}/scanfoldigv-macos.zip *
popd
