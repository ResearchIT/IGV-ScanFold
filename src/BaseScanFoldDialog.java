package org.broad.igv.scanfold;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.border.TitledBorder;

import org.broad.igv.Globals;
import org.broad.igv.batch.BatchRunner;
import org.broad.igv.batch.CommandExecutor;
import org.broad.igv.exceptions.DataLoadException;
import org.broad.igv.feature.Strand;
import org.broad.igv.feature.genome.Genome;
import org.broad.igv.feature.genome.GenomeManager;
import org.broad.igv.track.SequenceTrack;
import org.broad.igv.ui.IGV;
import org.broad.igv.ui.WaitCursorManager;
import org.broad.igv.ui.util.FileDialogUtils;
import org.broad.igv.ui.util.MessageUtils;
import org.broad.igv.util.LongRunningTask;
import org.broad.igv.util.ParsingUtils;
import org.broad.igv.util.RuntimeUtils;
import org.broad.igv.util.StringUtils;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JTextArea;
import java.awt.Rectangle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Canvas;
import javax.swing.ScrollPaneConstants;

public abstract class BaseScanFoldDialog extends JDialog {
	
	protected String sequence;
	protected String sequenceName;
	protected int sequenceStart;
	protected boolean resultsInNewWindow;
	
	PrintStream systemOutStream;
    PrintStream systemErrStream;

	/**
	 * Create the dialog.
	 */
	public BaseScanFoldDialog() {
		
        
	}
	

    public static String extractSequence(Genome genome, String chr, int start, int end, Strand strand) {
    	
    	String retSequence = "";
        try {
            //IGV.getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            byte[] seqBytes = genome.getSequence(chr, start, end);

            if (seqBytes == null) {
                MessageUtils.showMessage("Sequence not available");
            } else {
                String sequence = new String(seqBytes);

//                SequenceTrack sequenceTrack = IGV.getInstance().getSequenceTrack();
//                if (strand == Strand.NEGATIVE || (sequenceTrack != null && sequenceTrack.getStrand() == Strand.NEGATIVE)) {
//                    sequence = SequenceTrack.getReverseComplement(sequence);
//                }
                if (strand == Strand.NEGATIVE) {
                  sequence = SequenceTrack.getReverseComplement(sequence);
                }
                retSequence = sequence;
            }

        } finally {
        	
            //IGV.getMainFrame().setCursor(Cursor.getDefaultCursor());
        }
        
        return retSequence;
        
    }
    
    public static String getTempOuputDirectory() {
    	String outputDirectory;
    	try {
    		outputDirectory = Files.createTempDirectory("scanfold-results").toFile().getAbsolutePath();
    	}catch (IOException e) {
    		outputDirectory = (new File(System.getProperty("java.io.tmpdir"))).getAbsolutePath();
		}
    	return outputDirectory;
    }
    
	protected String writeSequenceToTempFile(String outputDirectory, String strand) {
		String sequence = this.sequence;
		if (strand.equals("reverse")) {
			sequence = SequenceTrack.getReverseComplement(sequence);
		}
		String tempFilePath = "";
		try {
			File tempDir = new File(outputDirectory);
			File tempFile = File.createTempFile("scanfoldinput", ".fa", tempDir);
			FileWriter fileWriter = new FileWriter(tempFile, true);
			BufferedWriter bw = new BufferedWriter(fileWriter);

			bw.write(sequence);
			bw.close();
			tempFilePath = tempFile.getAbsolutePath();
		} catch (IOException e) {
			showMessage(e.getMessage());
		}
		return tempFilePath;
	}
    
    protected abstract class IgvToolsSwingWorker extends SwingWorker{
    	
    	JButton runButton;
    	JTextArea outputText;
    	
    	protected IgvToolsSwingWorker(JTextArea outputText, JButton runButton) {
    		this.outputText = outputText;
    		this.runButton = runButton;
    	}
        @Override
        protected void done() {
            runButton.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
            updateTextArea(outputText, "Done");
        }
        
    }

    protected void updateTextArea(JTextArea outputText, final String text) {
    	updateTextArea(outputText, text, false);
    }
    
    protected void updateTextArea(JTextArea outputText, final String text, boolean addNewline) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputText.append(text);
                if (addNewline) {
                	outputText.append("\n");
                }
            }
        });
    }
   
    protected void redirectSystemStreams(JTextArea outputText) {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(outputText, String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(outputText, new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        systemOutStream = System.out;
        systemErrStream = System.err;

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
    
    protected void close() {
        System.setErr(systemErrStream);
        System.setOut(systemOutStream);
        dispose();
    }
    
    protected void showMessage(String tool) {
        JOptionPane.showMessageDialog(this, tool);
    }
    
    protected String executeShellCommand(JTextArea outputText, String cmd[], String[] envp, File dir, boolean waitFor) throws IOException {
        Process pr = RuntimeUtils.startExternalProcess(cmd, envp, dir);

        if(waitFor){
            try {
                pr.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        InputStream inputStream = null;
        String line = "";

        try {
            inputStream = pr.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream));
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            while ((line = buf.readLine()) != null) {
            	updateTextArea(outputText, line, true);
                pw.println(line);
            }
            pw.close();
            return writer.toString();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            OutputStream os = pr.getOutputStream();
            if(os != null){
                os.close();
            }
        }
    }

    protected void runBatchFile(String batchFile) {
		CommandExecutor cmdExe = new CommandExecutor(IGV.getInstance());
		BufferedReader reader = null;
		String inLine;
		try {
			reader = ParsingUtils.openBufferedReader(batchFile);
			while ((inLine = reader.readLine()) != null) {
				if (!(inLine.startsWith("#") || inLine.startsWith("//"))) {
					cmdExe.execute(inLine);
				}
			}
		} catch (IOException ioe) {
			throw new DataLoadException(ioe.getMessage(), batchFile);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					showMessage(e.getMessage());
				}
			}
		}
    }
    
}
