package org.broad.igv.scanfold;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.border.TitledBorder;

import org.broad.igv.ui.IGV;
import org.broad.igv.ui.util.FileDialogUtils;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextPane;

public class ScanFoldGui extends BaseScanFoldDialog {
		
	private final JPanel contentPanel = new JPanel();
	private JTextField windowSize;
	private JLabel lblWindowSize;
	private JTextField stepSize;
	private JLabel lblStepSize;
	private JTextField randomizations;
	private JTextField shuffleType;
	private JTextField temperature;
	private JTextField competition;
	private JLabel lblRandomizations;
	private JLabel lblShuffletype;
	private JLabel lblTemperature;
	private JLabel lblCompetition;
	private JButton runButton;
	private JButton closeButton;
	private JTextArea outputText;
	private JScrollPane outputScroll;
    private JCheckBox globalRefold;
    private JLabel lblStrand;
    private JComboBox strand;
    private JLabel lblAlgorithm;
    private JComboBox algorithm;
    private JLabel lblOutputDirectory;
    private JTextField outputDirectory;
    private JButton outputDirectoryBrowse;
    private JPanel descriptionPanel;
    private JTextPane windowDescriptionPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ScanFoldGui dialog = new ScanFoldGui();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Create the dialog.
	 */
	public ScanFoldGui() {
		//super(IGV.getMainFrame(), true);
		setTitle("scanfoldgui");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		{
			descriptionPanel = new JPanel();
			GridBagConstraints gbc_descriptionPanel = new GridBagConstraints();
			gbc_descriptionPanel.insets = new Insets(0, 0, 5, 0);
			gbc_descriptionPanel.fill = GridBagConstraints.BOTH;
			gbc_descriptionPanel.gridx = 0;
			gbc_descriptionPanel.gridy = 0;
			getContentPane().add(descriptionPanel, gbc_descriptionPanel);
			GridBagLayout gbl_descriptionPanel = new GridBagLayout();
			gbl_descriptionPanel.columnWidths = new int[] {600, 0};
			gbl_descriptionPanel.rowHeights = new int[]{21, 0};
			gbl_descriptionPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_descriptionPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			descriptionPanel.setLayout(gbl_descriptionPanel);
			{
				windowDescriptionPane = new JTextPane();
				windowDescriptionPane.setEditable(false);
				windowDescriptionPane.setText("Placeholder");
				GridBagConstraints gbc_windowDescriptionPane = new GridBagConstraints();
				gbc_windowDescriptionPane.fill = GridBagConstraints.BOTH;
				gbc_windowDescriptionPane.gridx = 0;
				gbc_windowDescriptionPane.gridy = 0;
				descriptionPanel.add(windowDescriptionPane, gbc_windowDescriptionPane);
			}
		}
		contentPanel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_contentPanel = new GridBagConstraints();
		gbc_contentPanel.fill = GridBagConstraints.BOTH;
		gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contentPanel.gridx = 0;
		gbc_contentPanel.gridy = 1;
		getContentPane().add(contentPanel, gbc_contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{119, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{38, 35, 47, 41, 34, 0, 23, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			lblWindowSize = new JLabel("Window Size");
			GridBagConstraints gbc_lblWindowSize = new GridBagConstraints();
			gbc_lblWindowSize.insets = new Insets(0, 0, 5, 5);
			gbc_lblWindowSize.anchor = GridBagConstraints.EAST;
			gbc_lblWindowSize.gridx = 0;
			gbc_lblWindowSize.gridy = 0;
			contentPanel.add(lblWindowSize, gbc_lblWindowSize);
		}
		{
			windowSize = new JTextField();
			windowSize.setToolTipText("This will adjust the window size (in nucleotides) which is used to canvas input sequence");
			lblWindowSize.setLabelFor(windowSize);
			windowSize.setText("120");
			GridBagConstraints gbc_windowSize = new GridBagConstraints();
			gbc_windowSize.insets = new Insets(0, 0, 5, 5);
			gbc_windowSize.fill = GridBagConstraints.HORIZONTAL;
			gbc_windowSize.gridx = 1;
			gbc_windowSize.gridy = 0;
			contentPanel.add(windowSize, gbc_windowSize);
			windowSize.setColumns(10);
		}
		{
			lblStepSize = new JLabel("Step Size");
			GridBagConstraints gbc_lblStepSize = new GridBagConstraints();
			gbc_lblStepSize.insets = new Insets(0, 0, 5, 5);
			gbc_lblStepSize.anchor = GridBagConstraints.EAST;
			gbc_lblStepSize.gridx = 0;
			gbc_lblStepSize.gridy = 1;
			contentPanel.add(lblStepSize, gbc_lblStepSize);
		}
		{
			stepSize = new JTextField();
			lblStepSize.setLabelFor(stepSize);
			stepSize.setToolTipText("");
			stepSize.setText("10");
			stepSize.setColumns(10);
			GridBagConstraints gbc_stepSize = new GridBagConstraints();
			gbc_stepSize.insets = new Insets(0, 0, 5, 5);
			gbc_stepSize.fill = GridBagConstraints.HORIZONTAL;
			gbc_stepSize.gridx = 1;
			gbc_stepSize.gridy = 1;
			contentPanel.add(stepSize, gbc_stepSize);
		}
		{
			lblRandomizations = new JLabel("Randomizations");
			GridBagConstraints gbc_lblRandomizations = new GridBagConstraints();
			gbc_lblRandomizations.insets = new Insets(0, 0, 5, 5);
			gbc_lblRandomizations.anchor = GridBagConstraints.EAST;
			gbc_lblRandomizations.gridx = 0;
			gbc_lblRandomizations.gridy = 2;
			contentPanel.add(lblRandomizations, gbc_lblRandomizations);
		}
		{
			randomizations = new JTextField();
			lblRandomizations.setLabelFor(randomizations);
			randomizations.setToolTipText("");
			randomizations.setText("30");
			randomizations.setColumns(10);
			GridBagConstraints gbc_randomizations = new GridBagConstraints();
			gbc_randomizations.insets = new Insets(0, 0, 5, 5);
			gbc_randomizations.fill = GridBagConstraints.HORIZONTAL;
			gbc_randomizations.gridx = 1;
			gbc_randomizations.gridy = 2;
			contentPanel.add(randomizations, gbc_randomizations);
		}
		{
			lblShuffletype = new JLabel("Shuffle Type");
			GridBagConstraints gbc_lblShuffletype = new GridBagConstraints();
			gbc_lblShuffletype.insets = new Insets(0, 0, 5, 5);
			gbc_lblShuffletype.anchor = GridBagConstraints.EAST;
			gbc_lblShuffletype.gridx = 0;
			gbc_lblShuffletype.gridy = 3;
			contentPanel.add(lblShuffletype, gbc_lblShuffletype);
		}
		{
			shuffleType = new JTextField();
			lblShuffletype.setLabelFor(shuffleType);
			shuffleType.setText("mono");
			shuffleType.setToolTipText("");
			GridBagConstraints gbc_shuffleType = new GridBagConstraints();
			gbc_shuffleType.insets = new Insets(0, 0, 5, 5);
			gbc_shuffleType.fill = GridBagConstraints.HORIZONTAL;
			gbc_shuffleType.gridx = 1;
			gbc_shuffleType.gridy = 3;
			contentPanel.add(shuffleType, gbc_shuffleType);
		}
		{
			lblTemperature = new JLabel("Temperature");
			GridBagConstraints gbc_lblTemperature = new GridBagConstraints();
			gbc_lblTemperature.insets = new Insets(0, 0, 5, 5);
			gbc_lblTemperature.anchor = GridBagConstraints.EAST;
			gbc_lblTemperature.gridx = 0;
			gbc_lblTemperature.gridy = 4;
			contentPanel.add(lblTemperature, gbc_lblTemperature);
		}
		{
			temperature = new JTextField();
			lblTemperature.setLabelFor(temperature);
			temperature.setToolTipText("");
			temperature.setText("37");
			GridBagConstraints gbc_temperature = new GridBagConstraints();
			gbc_temperature.insets = new Insets(0, 0, 5, 5);
			gbc_temperature.fill = GridBagConstraints.HORIZONTAL;
			gbc_temperature.gridx = 1;
			gbc_temperature.gridy = 4;
			contentPanel.add(temperature, gbc_temperature);
		}
		{
			lblCompetition = new JLabel("Competition");
			GridBagConstraints gbc_lblCompetition = new GridBagConstraints();
			gbc_lblCompetition.insets = new Insets(0, 0, 5, 5);
			gbc_lblCompetition.anchor = GridBagConstraints.EAST;
			gbc_lblCompetition.gridx = 0;
			gbc_lblCompetition.gridy = 5;
			contentPanel.add(lblCompetition, gbc_lblCompetition);
		}
		{
			competition = new JTextField();
			lblCompetition.setLabelFor(competition);
			competition.setToolTipText("");
			competition.setText("1");
			GridBagConstraints gbc_competition = new GridBagConstraints();
			gbc_competition.insets = new Insets(0, 0, 5, 5);
			gbc_competition.fill = GridBagConstraints.HORIZONTAL;
			gbc_competition.gridx = 1;
			gbc_competition.gridy = 5;
			contentPanel.add(competition, gbc_competition);
		}
		{
			lblStrand = new JLabel("Strand");
			GridBagConstraints gbc_lblStrand = new GridBagConstraints();
			gbc_lblStrand.anchor = GridBagConstraints.EAST;
			gbc_lblStrand.insets = new Insets(0, 0, 5, 5);
			gbc_lblStrand.gridx = 0;
			gbc_lblStrand.gridy = 6;
			contentPanel.add(lblStrand, gbc_lblStrand);
		}
		{
			strand = new JComboBox();
			strand.setModel(new DefaultComboBoxModel(new String[] {"forward", "reverse"}));
			strand.setSelectedIndex(0);
			strand.setToolTipText("");
			GridBagConstraints gbc_strand = new GridBagConstraints();
			gbc_strand.insets = new Insets(0, 0, 5, 5);
			gbc_strand.fill = GridBagConstraints.HORIZONTAL;
			gbc_strand.gridx = 1;
			gbc_strand.gridy = 6;
			contentPanel.add(strand, gbc_strand);
		}
		{
			lblAlgorithm = new JLabel("Algorithm");
			GridBagConstraints gbc_lblAlgorithm = new GridBagConstraints();
			gbc_lblAlgorithm.anchor = GridBagConstraints.EAST;
			gbc_lblAlgorithm.insets = new Insets(0, 0, 5, 5);
			gbc_lblAlgorithm.gridx = 0;
			gbc_lblAlgorithm.gridy = 7;
			contentPanel.add(lblAlgorithm, gbc_lblAlgorithm);
		}
		{
			algorithm = new JComboBox();
			algorithm.setModel(new DefaultComboBoxModel(new String[] {"rnafold", "rnastructure"}));
			algorithm.setSelectedIndex(0);
			algorithm.setToolTipText("");
			GridBagConstraints gbc_algorithm = new GridBagConstraints();
			gbc_algorithm.insets = new Insets(0, 0, 5, 5);
			gbc_algorithm.fill = GridBagConstraints.HORIZONTAL;
			gbc_algorithm.gridx = 1;
			gbc_algorithm.gridy = 7;
			contentPanel.add(algorithm, gbc_algorithm);
		}
		{
			lblOutputDirectory = new JLabel("Output Folder");
			GridBagConstraints gbc_lblOutputDirectory = new GridBagConstraints();
			gbc_lblOutputDirectory.anchor = GridBagConstraints.EAST;
			gbc_lblOutputDirectory.insets = new Insets(0, 0, 5, 5);
			gbc_lblOutputDirectory.gridx = 0;
			gbc_lblOutputDirectory.gridy = 8;
			contentPanel.add(lblOutputDirectory, gbc_lblOutputDirectory);
		}
		{
			outputDirectory = new JTextField();
			outputDirectory.setText(getTempOuputDirectory());
			lblOutputDirectory.setLabelFor(outputDirectory);
			GridBagConstraints gbc_outputDirectory = new GridBagConstraints();
			gbc_outputDirectory.insets = new Insets(0, 0, 5, 5);
			gbc_outputDirectory.fill = GridBagConstraints.HORIZONTAL;
			gbc_outputDirectory.gridx = 1;
			gbc_outputDirectory.gridy = 8;
			contentPanel.add(outputDirectory, gbc_outputDirectory);
		}
		{
			outputDirectoryBrowse = new JButton("Browse");
			GridBagConstraints gbc_outputDirectoryBrowse = new GridBagConstraints();
			gbc_outputDirectoryBrowse.insets = new Insets(0, 0, 5, 0);
			gbc_outputDirectoryBrowse.gridx = 2;
			gbc_outputDirectoryBrowse.gridy = 8;
			outputDirectoryBrowse.addActionListener(e -> {
	            try {
	                File chosenFile = FileDialogUtils.chooseDirectory("Choose Output Directory:", new File(outputDirectory.getText()));
	                if (!chosenFile.isDirectory()) {
	                	chosenFile = chosenFile.getParentFile();
	                }
	                outputDirectory.setText(chosenFile.getAbsolutePath());
	            } catch (NullPointerException npe) {
	            }
	        });
			contentPanel.add(outputDirectoryBrowse, gbc_outputDirectoryBrowse);
		}
		{
			globalRefold = new JCheckBox("Global Refold");
			globalRefold.setSelected(true);
			globalRefold.setToolTipText("When checked, this option will refold your entire sequence while constraining the most significant base pairs (with Zavg values < -1 and < -2).\n");
			GridBagConstraints gbc_globalRefold = new GridBagConstraints();
			gbc_globalRefold.insets = new Insets(0, 0, 0, 5);
			gbc_globalRefold.anchor = GridBagConstraints.WEST;
			gbc_globalRefold.gridx = 0;
			gbc_globalRefold.gridy = 9;
			contentPanel.add(globalRefold, gbc_globalRefold);
		}

		{
			JPanel outputPanel = new JPanel();
			outputPanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Log Output", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			GridBagConstraints gbc_outputPanel = new GridBagConstraints();
			gbc_outputPanel.fill = GridBagConstraints.BOTH;
			gbc_outputPanel.insets = new Insets(0, 0, 5, 0);
			gbc_outputPanel.gridx = 0;
			gbc_outputPanel.gridy = 2;
			getContentPane().add(outputPanel, gbc_outputPanel);
			GridBagLayout gbl_outputPanel = new GridBagLayout();
			gbl_outputPanel.columnWidths = new int[] {600, 0};
			gbl_outputPanel.rowHeights = new int[] {300, 0};
			gbl_outputPanel.columnWeights = new double[]{0.0};
			gbl_outputPanel.rowWeights = new double[]{0.0};
			outputPanel.setLayout(gbl_outputPanel);
			{
				outputScroll = new JScrollPane();
				GridBagConstraints gbc_outputScroll = new GridBagConstraints();
				gbc_outputScroll.anchor = GridBagConstraints.NORTHWEST;
				gbc_outputScroll.fill = GridBagConstraints.BOTH;
				gbc_outputScroll.gridx = 0;
				gbc_outputScroll.gridy = 0;
				outputPanel.add(outputScroll, gbc_outputScroll);
			}
			{
				outputText = new JTextArea();
				outputText.setColumns(50);
				GridBagConstraints gbc_outputText = new GridBagConstraints();
				gbc_outputText.insets = new Insets(0, 0, 5, 0);
				gbc_outputText.fill = GridBagConstraints.BOTH;
				gbc_outputText.gridx = 0;
				gbc_outputText.gridy = 1;
				outputText.setEditable(false);
                outputText.setText("");
                outputText.setRows(10);
                outputScroll.setViewportView(outputText);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			buttonPane.setLayout(fl_buttonPane);
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 3;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				runButton = new JButton("Run");
				runButton.addActionListener(e -> run());
				buttonPane.add(runButton);
				getRootPane().setDefaultButton(runButton);
			}
			{
				closeButton = new JButton("Close");
				closeButton.addActionListener(e -> close());
				buttonPane.add(closeButton);
			}
		}
		
        addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(WindowEvent windowEvent) {
                close();
            }
        });
        
        redirectSystemStreams(outputText);
        
        setMinimumSize(new Dimension(600,850));
        setResizable(false);
	}
	
    public static void launch(String chr, int start, String sequence, boolean resultsInNewWindow, String launchPoint) {
        ScanFoldGui mainWindow = new ScanFoldGui();
        mainWindow.sequenceName = chr;
        mainWindow.sequence = sequence;
        mainWindow.sequenceStart = start;
        mainWindow.resultsInNewWindow = resultsInNewWindow;

        if (launchPoint.equals("roi")) {
        	mainWindow.windowDescriptionPane.setText("Running scanfold on the selected Region of Interest.\r\nTo run ScanFold on the entire visible region, close this window, then click 'ScanFold' in the main IGV menu bar.");
        } else {
        	mainWindow.windowDescriptionPane.setText("Running scanfold on the visible region.\r\nTo run ScanFold on an IGV 'Region of Interest', close this window, then right click the red 'Region of Interest' bar.");
        }
        
        mainWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainWindow.setVisible(true);
    }
    
    
    private void run() {
		runButton.setEnabled(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingWorker swingWorker = new IgvToolsSwingWorker(outputText, runButton) {

			@Override
			protected Object doInBackground() {
				try {
					
					String inputFile = writeSequenceToTempFile(outputDirectory.getText(), (String) strand.getSelectedItem());
					
					Map<String, String> env = System.getenv();
					
					ArrayList<String> cmd = new ArrayList<>(Arrays.asList(new String[] {
							env.get("SCANFOLDPYTHONINTERPRETER"),
							env.get("SCANFOLDRUNSCRIPT"),
							"-i", inputFile,
							"-o", outputDirectory.getText(),
							"-n", sequenceName,
							"-d", (String) strand.getSelectedItem(),
							"-z", String.valueOf(sequenceStart),
							"scanfold",
							"-c", competition.getText(),
							"-s", stepSize.getText(),
							"-w", windowSize.getText(),
							"-r", randomizations.getText(),
							"-y", shuffleType.getText(),
							"-t", temperature.getText(),
							"-a", (String) algorithm.getSelectedItem(),
							
					}));
					
					if (globalRefold.isSelected()) {
						cmd.add("-g");
					}
					
					if (resultsInNewWindow) {
						cmd.add("-j");
						cmd.add(new File(ScanFoldGui.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
					}
					
					
					String result = executeShellCommand(outputText, cmd.toArray(new String[cmd.size()]), null, new File(env.get("SCANFOLDRUNDIR")), false);
					
					if (!resultsInNewWindow) {
						String startSentinel = "BATCHFILEFIRSTSENTINEL";
						String endSentinel = "BATCHFILESECONDSENTINEL";
						String batchFile = result.substring(result.indexOf(startSentinel) + startSentinel.length(), result.indexOf(endSentinel));
						runBatchFile(batchFile);
					}
					
				} catch (IOException e) {
					showMessage(e.getMessage());
				} catch (URISyntaxException e) {
					showMessage(e.getMessage());
				}
				return null;
			}
		};

		swingWorker.execute();
    }

}
