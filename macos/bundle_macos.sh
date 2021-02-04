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
unzip -d ${BUNDLE_PREFIX} distributions/IGV_MacApp_user_WithJava.zip
#mv build ${BUNDLE_PREFIX}/IGV
popd

# main
pushd ${BUNDLE_PREFIX}
zip -r ${TOPLEVEL}/scanfoldigv-macos.zip *
popd
