# scanfoldigvpatch

Patched version of IGV that integrates ScanFold functionality.

## Requirements

* python3
* jdk 11 (newer may work, haven't tested)

### fedora
```
sudo dnf install git zip patch java-11-openjdk.x86_64 gcc g++ python3-devel zlib-devel tar make
```

## Compile

### Build IGV (CI currently only does this)

1. clone this repository recursively
1. replace all instances of `/home/njbooher/workspace/repos/scanfoldigv` in the `src/ScanFoldGui.java` with the path where you cloned this repository  
   TODO: fix this
1. `./scripts/apply_patch.sh`
1. build IGV as normal (run `./gradlew createDist` in the igv directory)

### Build ScanFold and ViennaRNA

1. `./scripts/build_scanfold_env.sh`
1. `./scripts/build_viennarna.sh`  
    this will fail at src/Kinfold, but that's fine
1. `./scripts/build_rnastructure.sh`

## Run

1. `./igv/build/IGV-dist/igv.sh`
