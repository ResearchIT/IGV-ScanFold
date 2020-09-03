# scanfoldigvpatch

## Build IGV (CI currently only does this)

1. clone this repository recursively
1. `./scripts/apply_patch.sh`
1. build IGV as normal (run `./gradlew createDist` in the igv directory)

## Build ScanFold and ViennaRNA

1. `./scripts/build_scanfold_env.sh`
1. `./scripts/build_viennarna.sh`
1. `./scripts/build_rnastructure.sh`

## Run IGV

1. `./igv/build/IGV-dist/igv.sh`