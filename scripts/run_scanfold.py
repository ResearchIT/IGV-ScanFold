import sys
import os
#sys.stderr = open(os.devnull,'w')

import argparse
import subprocess
import tempfile
import platform

def file_is_empty(file_path):
    return os.path.exists(file_path) and os.stat(file_path).st_size == 0

def mktemp(directory, extension, name="output"):
    file_handle, file_path = tempfile.mkstemp(prefix=name, suffix=extension, dir=directory)
    os.close(file_handle)
    return file_path

def run_me(proc_env, command, workdir):
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

def make_env():
    proc_env = os.environ.copy()
    cwd = os.getcwd()

    if platform.system() == 'Windows':
        proc_env['DATAPATH'] = os.path.join(cwd, 'RNAstructure', 'data_tables')
        new_path = [
            os.path.join(cwd, 'ViennaRNA'),
            os.path.join(cwd, 'RNAstructure'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ';'.join(new_path)
    elif platform.system() == 'Darwin' and 'SCANFOLDISBUNDLED' in os.environ:
        proc_env['DATAPATH'] = os.path.join(cwd, 'RNAstructure', 'data_tables')
        new_path = [
            os.path.join(cwd, 'ViennaRNA'),
            os.path.join(cwd, 'RNAstructure'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ':'.join(new_path)
    else:
        proc_env['DATAPATH'] = os.path.join(cwd, 'env', 'data_tables')
        proc_env['VIRTUAL_ENV'] = os.path.join(cwd, 'env')
        new_path = [
            os.path.join(cwd, 'env', 'bin'),
            proc_env['PATH']
        ]
        proc_env['PATH'] = ':'.join(new_path)

    return proc_env

def make_scanfold_command(script, args):
    cwd = os.getcwd()

    if platform.system() == 'Windows':
        script = os.path.join(cwd, 'scanfold', script + ".py")
        python_interpreter = os.environ['SCANFOLDPYTHONINTERPRETER']
        command = [python_interpreter, '-u', script]
    elif platform.system() == 'Darwin' and 'SCANFOLDISBUNDLED' in os.environ:
        python_interpreter = os.environ['SCANFOLDPYTHONINTERPRETER']
        if script == "ScanFold-Scan_IGV":
            command = [os.path.join(cwd, 'scanfold', 'ScanFold-Scan_IGV.dist', 'ScanFold-Scan_IGV')]
        else:
            command = [os.path.join(cwd, 'scanfold', 'ScanFold-Fold_IGV.dist', 'ScanFold-Fold_IGV')]
    else:
        script = os.path.join(cwd, 'ScanFold', script + ".py")
        python_interpreter = 'python'
        command = [python_interpreter, '-u', script]
    command.extend(args)
    return command

def main_scanfold(args):

    proc_env = make_env()

    SCANOUTPATH = mktemp(args.WORKDIR, '.scan-out.tsv')
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
        '--fasta_file_path', FASTAFILEPATH,
        '--fasta_index', FASTAINDEX,
        '--algo', args.ALGORITHM
    ]

    run_me(proc_env, make_scanfold_command('ScanFold-Scan_IGV', scan_params), args.WORKDIR)

    OUT1 = mktemp(args.WORKDIR, '.nofilter.ct')
    OUT2 = mktemp(args.WORKDIR, '.-1filter.ct')
    OUT3 = mktemp(args.WORKDIR, '.-2filter.ct')
    LOGFILE = mktemp(args.WORKDIR, '.log.txt')
    FINALPARTNERS = mktemp(args.WORKDIR, '.final_partners.txt')
    BPTRACK = mktemp(args.WORKDIR, '.IGV.bp')
    FASTATRACK = mktemp(args.WORKDIR, '.input.fa')
    #DBNFILEPATH = mktemp(args.WORKDIR, '.-2filter.dbn')
    DBNFILEPATH = mktemp(args.WORKDIR, '.-2filter.dbn')
    DBNFILEPATH1 = mktemp(args.WORKDIR, '.dbnfile1.dbn')
    DBNFILEPATH2 = mktemp(args.WORKDIR, '.dbnfile2.dbn')
    DBNFILEPATH3 = mktemp(args.WORKDIR, '.dbnfile3.dbn')
    DBNFILEPATH4 = mktemp(args.WORKDIR, '.DBNstructures.txt')
    ZSCOREWIGFILEPATH = mktemp(args.WORKDIR, '.zscore.wig')
    MFEWIGFILEPATH = mktemp(args.WORKDIR, '.mfe.wig')
    EDWIGFILEPATH = mktemp(args.WORKDIR, '.ed.wig')
    #PVALUEWIGFILEPATH = mktemp(args.WORKDIR, '.pvalue.wig')
    STRUCTUREEXTRACTFILE = mktemp(args.WORKDIR, '.ExtractedStructures.gff3')
    #FINALPARTNERSWIG = mktemp(args.WORKDIR, '.final_partners_zscore.wig')
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
        '--zscore_wig_file_path', ZSCOREWIGFILEPATH,
        '--mfe_wig_file_path', MFEWIGFILEPATH,
        '--ed_wig_file_path', EDWIGFILEPATH,
        #'--pvalue_wig_file_path', PVALUEWIGFILEPATH,
        '--structure_extract_file', STRUCTUREEXTRACTFILE,
        '--fasta_index', FASTAINDEX,
        '--algo', args.ALGORITHM
    ]

    if args.GLOBALREFOLD:
        fold_params.append('--global_refold')

    run_me(proc_env, make_scanfold_command('ScanFold-Fold_IGV', fold_params), args.WORKDIR)

    files_to_maybe_load = [
        BPTRACK,
        ZSCOREWIGFILEPATH,
        MFEWIGFILEPATH,
        EDWIGFILEPATH,
        STRUCTUREEXTRACTFILE,
    ]

    if (not file_is_empty(ZSCOREWIGFILEPATH) and (not file_is_empty(DBNFILEPATH))):
        VARNA_FILE = mktemp(args.WORKDIR, '.scanfoldvarna')
        with open(VARNA_FILE, "w") as output_file:
            output_file.write(args.STRAND + "\n")
            output_file.write(DBNFILEPATH + "\n")
            output_file.write(ZSCOREWIGFILEPATH + "\n")
        files_to_maybe_load.append(VARNA_FILE)

    load_files(files_to_maybe_load)

def main_rnastructure(args):
    proc_env = make_env()
    DBNFILEPATH = mktemp(args.WORKDIR, '.dbn')
    temp_kelvin = int(args.TEMPERATURE)+273.15
    if platform.system() == 'Windows':
        command_path = os.path.join(os.getcwd(), "RNAstructure", "Fold")
    else:
        command_path = "Fold"
    command = [command_path, "-k", "-mfe", "-T", str(temp_kelvin), args.INPUTFILE, DBNFILEPATH]
    run_me(proc_env, command, args.WORKDIR)

    files_to_maybe_load = []

    if (not file_is_empty(DBNFILEPATH)):
        VARNA_FILE = mktemp(args.WORKDIR, '.scanfoldlessdbn')
        with open(VARNA_FILE, "w") as output_file:
            output_file.write(args.SEQUENCENAME + "\n")
            output_file.write(args.STRAND + "\n")
            output_file.write(str(args.STARTPOS) + "\n")
            output_file.write(DBNFILEPATH + "\n")
        files_to_maybe_load.append(VARNA_FILE)

    load_files(files_to_maybe_load)

def main_rnafold(args):
    proc_env = make_env()
    LOG = mktemp(args.WORKDIR, '.log')
    if platform.system() == 'Windows':
        command_path = os.path.join(os.getcwd(), "ViennaRNA", "RNAfold")
    else:
        command_path = "RNAfold"
    command = [
        command_path,
        "-T", str(args.TEMPERATURE),
        '-i', args.INPUTFILE,
        '--maxBPspan', args.MAXBPSPAN,
        '-o', LOG]
    run_me(proc_env, command, args.WORKDIR)

    DBNFILEPATH = os.path.join(args.WORKDIR, 'RNAfold_output.fold')

    files_to_maybe_load = []

    if (not file_is_empty(DBNFILEPATH)):
        VARNA_FILE = mktemp(args.WORKDIR, '.scanfoldlessdbn')
        with open(VARNA_FILE, "w") as output_file:
            output_file.write(args.SEQUENCENAME + "\n")
            output_file.write(args.STRAND + "\n")
            output_file.write(str(args.STARTPOS) + "\n")
            output_file.write(DBNFILEPATH + "\n")
        files_to_maybe_load.append(VARNA_FILE)

    load_files(files_to_maybe_load)


def load_files(files_to_maybe_load):
    files_to_load = [maybe_file for maybe_file in files_to_maybe_load if not file_is_empty(maybe_file)]

    batch_file_path = os.path.join(args.WORKDIR, 'batchfile.txt')

    with open(batch_file_path, 'w') as batch_file:
        template = "load \"{}\"\n"
        for file_to_load in files_to_load:
            batch_file.write(template.format(file_to_load))

    print("BATCHFILEFIRSTSENTINEL{}BATCHFILESECONDSENTINEL".format(batch_file_path))

if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument('-i', '--INPUTFILE', type=str)
    parser.add_argument('-o', '--WORKDIR', type=str)
    parser.add_argument('-n', '--SEQUENCENAME', type=str)
    parser.add_argument('-d', '--STRAND', type=str)
    parser.add_argument('-z', '--STARTPOS', type=str)
    parser.add_argument('-t', '--TEMPERATURE', type=str)

    subparsers = parser.add_subparsers(help='sub-command help')

    scanfold = subparsers.add_parser('scanfold', help='scanfold')
    scanfold.add_argument('-a', '--ALGORITHM', type=str)
    scanfold.add_argument('-c', '--COMPETITION', type=str)
    scanfold.add_argument('-g', '--GLOBALREFOLD', action='store_true')
    scanfold.add_argument('-r', '--RANDOMIZATIONS', type=str)
    scanfold.add_argument('-s', '--STEPSIZE', type=str)
    scanfold.add_argument('-w', '--WINDOWSIZE', type=str)
    scanfold.add_argument('-y', '--RANDOMIZATIONTYPE', type=str)
    scanfold.set_defaults(func=main_scanfold)

    rnastructure = subparsers.add_parser('rnastructure', help='rnastructure')
    rnastructure.set_defaults(func=main_rnastructure)

    rnafold = subparsers.add_parser('rnafold', help='rnafold')
    rnafold.add_argument('-b', '--MAXBPSPAN', type=str)
    rnafold.set_defaults(func=main_rnafold)

    args = parser.parse_args()
    args.func(args)
