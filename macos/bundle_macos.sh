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
./gradlew createMacWithJavaDistZip -PjdkBundleWindows=${TOPLEVEL}/lib/jdk-11.0.10+9-jre/Contents/Home
mv build/IGV-MacApp-dist/IGV_user ${BUNDLE_PREFIX}/IGV
popd
