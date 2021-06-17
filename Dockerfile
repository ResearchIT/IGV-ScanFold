FROM registry.fedoraproject.org/fedora:34-x86_64

RUN INSTALL_PKGS="git zip patch java-11-openjdk.x86_64 gcc gcc-c++ python3-devel zlib-devel tar make which xvfb" && \
    yum install -y --setopt=tsflags=nodocs $INSTALL_PKGS --nogpgcheck && \
    rpm -V $INSTALL_PKGS && \
    yum -y clean all --enablerepo='*'

RUN mkdir /opt/IGV-ScanFold && chown 1001 /opt/IGV-ScanFold
USER 1001

WORKDIR /opt
RUN git clone --recursive https://github.com/ResearchIT/IGV-ScanFold.git
WORKDIR /opt/IGV-ScanFold
RUN ./scripts/apply_patch.sh
RUN ./scripts/build_igv.sh
# RUN ./scripts/build_scanfold_env.sh
# RUN ./scripts/build_viennarna.sh
# RUN ./scripts/build_rnastructure.sh
CMD xvfb-run ./scripts/run_igv.sh