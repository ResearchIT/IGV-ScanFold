# scanfoldigvpatch

Patched version of IGV that integrates ScanFold functionality.

## Run

### Windows
A pre-built version for Windows is available on [releases](https://github.com/ResearchIT/scanfoldigv/releases).

### Mac and Linux

Follow instructions to compile. Then

1. `./scripts/run_igv.sh`

## Compile

### Requirements

#### OSX
* X-code cli tools
* python3
* jdk 11 (newer may work, haven't tested)

#### Linux

* python3
* jdk 11 (newer may work, haven't tested)

##### on fedora
```
sudo dnf install git zip patch java-11-openjdk.x86_64 gcc g++ python3-devel zlib-devel tar make
```

### Build IGV (CI currently only does this)

1. clone this repository recursively  
    `git clone --recursive https://github.com/ResearchIT/scanfoldigv.git`
1. `./scripts/apply_patch.sh`
1. `./scripts/build_igv.sh`

### Build ScanFold and dependencies

1. `./scripts/build_scanfold_env.sh`
1. `./scripts/build_viennarna.sh`  
    this will fail at src/Kinfold, but that's fine
1. `./scripts/build_rnastructure.sh`