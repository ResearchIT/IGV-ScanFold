if [ ! -f lib/VARNAv3.jar ]; then
    ./scripts/prepare_varna.sh
fi
./scripts/clean_submodule.sh
mkdir -p igv/lib
cp lib/VARNAv3.jar igv/lib
ln -s ../../../../../../../src igv/src/main/java/org/broad/igv/scanfold
pushd igv
patch -p1 < ../scanfoldmenu.patch
popd