![CI](https://github.com/ResearchIT/IGV-ScanFold/actions/workflows/gradle.yml/badge.svg) ![Windows Package](https://github.com/ResearchIT/IGV-ScanFold/actions/workflows/windows.yml/badge.svg) ![MacOS Package](https://github.com/ResearchIT/IGV-ScanFold/actions/workflows/macos.yml/badge.svg)

# IGV-ScanFold

Patched version of IGV that integrates ScanFold functionality.

## Run

### Windows
A pre-built version is available on [releases](https://github.com/ResearchIT/IGV-ScanFold/releases).

### Mac (10.15+, x86_64)
A pre-built version is available on [releases](https://github.com/ResearchIT/IGV-ScanFold/releases).

Please open the Terminal app and run the following to download and run it.

```
cd ~/Downloads/
curl -L -O https://github.com/ResearchIT/IGV-ScanFold/releases/download/v0.2.3/IGV-ScanFold-macos-v0.2.3.zip
unzip IGV-ScanFold-macos-v0.2.3.zip
open -a IGV-ScanFold.app
```

### Linux

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
    `git clone --recursive https://github.com/ResearchIT/IGV-ScanFold.git`
1. `./scripts/apply_patch.sh`
1. `./scripts/build_igv.sh`

### Build ScanFold and dependencies

1. `./scripts/build_scanfold_env.sh`
1. `./scripts/build_viennarna.sh`  
    this may fail at src/Kinfold, but that's fine
1. `./scripts/build_rnastructure.sh`

### Links to IGV-ScanFold User Walkthroughs

1.  https://structurome.bb.iastate.edu/igv-scanfold-supplemental-data
2.  https://www.youtube.com/playlist?list=PL-FLz2olO5ubN8hKbeccZ6rfwBjfmTSyv
