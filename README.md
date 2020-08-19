# scanfoldigvpatch

## IGV build directions (CI currently only does this)

1. clone this repository recursively
1. `./scripts/apply_patch.sh`
1. build IGV as normal (run `./gradlew createDist` in the igv directory)

## ScanFold install instructions

1. `./scripts/build_scanfold_env.sh`
1. `./scripts/run_scanfold.sh`