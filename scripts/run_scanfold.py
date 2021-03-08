import sys
import os
sys.stderr = open(os.devnull,'w')

import argparse
import subprocess
import tempfile
import platform

def file_is_empty(file_path):
    return os.path.exists(file_path) and os.stat(file_path).st_size == 0

def run_me(script, workdir, args):

    proc_env = os.environ.copy()
    cwd = os.getcwd()

    if platform.system() == 'Windows':
        script = os.path.join(cwd, 'scanfold', script + ".py")
        proc_env['DATAPATH'] = os.path.join(cwd, 'RNAstructure', 'data_tables')
        new_path = [
            os.path.join(cwd, 'ViennaRNA'),
            os.path.join(cwd, 'RNAstructure'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ';'.join(new_path)
        python_interpreter = os.environ['SCANFOLDPYTHONINTERPRETER']
        command = [python_interpreter, '-u', script]
    elif platform.system() == 'Darwin' and 'SCANFOLDISBUNDLED' in os.environ:
        proc_env['DATAPATH'] = os.path.join(cwd, 'RNAstructure', 'data_tables')
        new_path = [
            os.path.join(cwd, 'ViennaRNA'),
            os.path.join(cwd, 'RNAstructure'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ':'.join(new_path)
        python_interpreter = os.environ['SCANFOLDPYTHONINTERPRETER']
        if script == "ScanFold-Scan_IGV":
            command = [os.path.join(cwd, 'scanfold', 'ScanFold-Scan_IGV.dist', 'ScanFold-Scan_IGV')]
        else:
            command = [os.path.join(cwd, 'scanfold', 'ScanFold-Fold_IGV.dist', 'ScanFold-Fold_IGV')]
    else:
        script = os.path.join(cwd, 'ScanFold', script + ".py")
        proc_env['DATAPATH'] = os.path.join(cwd, 'env', 'data_tables')
        proc_env['VIRTUAL_ENV'] = os.path.join(cwd, 'env')
        new_path = [
            os.path.join(cwd, 'env', 'bin'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ':'.join(new_path)
        python_interpreter = 'python'
        command = [python_interpreter, '-u', script]
    
    command.extend(args)

    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=open(os.devnull,'w'), env=proc_env, cwd=workdir, bufsize=0)
    for c in iter(lambda: process.stdout.read(1), b''): 
        if hasattr(sys.stdout, 'buffer'):
            # python 3
            sys.stdout.buffer.write(c)
            sys.stdout.buffer.flush()
        else:
            # python 2 (macos)
            sys.stdout.write(c)
            sys.stdout.flush()

def mktemp(directory, extension, name="output"):
    file_handle, file_path = tempfile.mkstemp(prefix=name, suffix=extension, dir=directory)
    os.close(file_handle)
    return file_path

def main(args):

    SCANOUTPATH = mktemp(args.WORKDIR, '.scan-out.tsv')
    ZSCOREWIGFILEPATH = mktemp(args.WORKDIR, '.zscore.wig')
    MFEWIGFILEPATH = mktemp(args.WORKDIR, '.mfe.wig')
    EDWIGFILEPATH = mktemp(args.WORKDIR, '.ed.wig')
    PVALUEWIGFILEPATH = mktemp(args.WORKDIR, '.pvalue.wig')
    FASTAFILEPATH = mktemp(args.WORKDIR, '.fasta', name='input')
    FASTAINDEX = mktemp(args.WORKDIR, '.fasta.fai', name='input')

    scan_params = [
        '-i', args.INPUTFILE,
        '-r', args.RANDOMIZATIONS,
        '-s', args.STEPSIZE,
        '-t', args.TEMPERATURE,
        '-w', args.WINDOWSIZE,
        '-d', args.STRAND,
        '--start', args.STARTPOS,
        '--name', args.SEQUENCENAME,
        '-type', args.RANDOMIZATIONTYPE,
        '--scan_out_path', SCANOUTPATH,
        '--zscore_wig_file_path', ZSCOREWIGFILEPATH,
        '--mfe_wig_file_path', MFEWIGFILEPATH,
        '--ed_wig_file_path', EDWIGFILEPATH,
        '--pvalue_wig_file_path', PVALUEWIGFILEPATH,
        '--fasta_file_path', FASTAFILEPATH,
        '--fasta_index', FASTAINDEX
    ]

    run_me('ScanFold-Scan_IGV', args.WORKDIR, scan_params)

    OUT1 = mktemp(args.WORKDIR, '.nofilter.ct')
    OUT2 = mktemp(args.WORKDIR, '.-1filter.ct')
    OUT3 = mktemp(args.WORKDIR, '.-2filter.ct')
    LOGFILE = mktemp(args.WORKDIR, '.log.txt')
    FINALPARTNERS = mktemp(args.WORKDIR, '.final_partners.txt')
    BPTRACK = mktemp(args.WORKDIR, '.IGV.bp')
    FASTATRACK = mktemp(args.WORKDIR, '.input.fa')
    #DBNFILEPATH = mktemp(args.WORKDIR, '.-2filter.dbn')
    DBNFILEPATH = mktemp(args.WORKDIR, '.-2filter.scanfoldvarna')
    DBNFILEPATH1 = mktemp(args.WORKDIR, '.dbnfile1.dbn')
    DBNFILEPATH2 = mktemp(args.WORKDIR, '.dbnfile2.dbn')
    DBNFILEPATH3 = mktemp(args.WORKDIR, '.dbnfile3.dbn')
    DBNFILEPATH4 = mktemp(args.WORKDIR, '.DBNstructures.txt')
    STRUCTUREEXTRACTFILE = mktemp(args.WORKDIR, '.ExtractedStructures.txt')
    FINALPARTNERSWIG = mktemp(args.WORKDIR, '.final_partners_zscore.wig')
    FASTAINDEX = mktemp(args.WORKDIR, '.fai')

    fold_params = [
        '-i', SCANOUTPATH,
        '-d', args.STRAND,
        '--name', args.SEQUENCENAME,
        '-c', args.COMPETITION,
        '--out1', OUT1,
        '--out2', OUT2,
        '--out3', OUT3,
        '--out4', LOGFILE,
        '--out5', FINALPARTNERS,
        '--out6', BPTRACK,
        '--out7', FASTATRACK,
        '--dbn_file_path', DBNFILEPATH,
        '--dbn_file_path1', DBNFILEPATH1,
        '--dbn_file_path2', DBNFILEPATH2,
        '--dbn_file_path3', DBNFILEPATH3,
        '--dbn_file_path4', DBNFILEPATH4,
        '--final_partners_wig', FINALPARTNERSWIG,
        '--structure_extract_file', STRUCTUREEXTRACTFILE,
        '--fasta_index', FASTAINDEX
    ]

    if args.GLOBALREFOLD:
        fold_params.append('--global_refold')

    run_me('ScanFold-Fold_IGV', args.WORKDIR, fold_params)

    files_to_maybe_load = [
        BPTRACK,
        FINALPARTNERSWIG,
        MFEWIGFILEPATH,
        ZSCOREWIGFILEPATH,
        PVALUEWIGFILEPATH,
        EDWIGFILEPATH,
        DBNFILEPATH,
    ]
    
    files_to_load = [maybe_file for maybe_file in files_to_maybe_load if not file_is_empty(maybe_file)]

    batch_file_path = os.path.join(args.WORKDIR, 'batchfile.txt')

    with open(batch_file_path, 'w') as batch_file:
        template = "load \"{}\"\n"
        for file_to_load in files_to_load:
            batch_file.write(template.format(file_to_load))

    print("BATCHFILEFIRSTSENTINEL{}BATCHFILESECONDSENTINEL".format(batch_file_path))


if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    
    parser.add_argument('-c', '--COMPETITION', type=str)
    parser.add_argument('-g', '--GLOBALREFOLD', action='store_true')
    parser.add_argument('-i', '--INPUTFILE', type=str)
    parser.add_argument('-n', '--SEQUENCENAME', type=str)
    parser.add_argument('-r', '--RANDOMIZATIONS', type=str)
    parser.add_argument('-s', '--STEPSIZE', type=str)
    parser.add_argument('-t', '--TEMPERATURE', type=str)
    parser.add_argument('-w', '--WINDOWSIZE', type=str)
    parser.add_argument('-y', '--RANDOMIZATIONTYPE', type=str)
    parser.add_argument('-z', '--STARTPOS', type=str)
    parser.add_argument('-d', '--STRAND', type=str)
    parser.add_argument('-o', '--WORKDIR', type=str)

    args = parser.parse_args()
    
    main(args)