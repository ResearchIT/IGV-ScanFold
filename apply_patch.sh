./clean_submodule.sh
ln -s ../../../../../../../src igv/src/main/java/org/broad/igv/scanfold
pushd igv
patch -p1 < ../scanfoldmenu.patch
popd