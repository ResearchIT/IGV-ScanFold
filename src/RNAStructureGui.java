package org.broad.igv.scanfold;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JFrame;
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

public class RNAStructureGui extends BaseScanFoldDialog {
		
	private final JPanel contentPanel = new JPanel();
	private JButton runButton;
	private JButton closeButton;
	private JTextArea outputText;
	private JScrollPane outputScroll;
    private JLabel lblStrand;
    private JComboBox strand;
    private JLabel lblTemperature;
    private JTextField temperature;
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
			RNAStructureGui dialog = new RNAStructureGui();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RNAStructureGui() {
		//super(IGV.getMainFrame(), true);
		setTitle("rnastructuregui");
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
				gbc_outputScroll.fill = GridBagConstraints.BOTH;
				gbc_outputScroll.anchor = GridBagConstraints.NORTHWEST;
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
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
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
        RNAStructureGui mainWindow = new RNAStructureGui();
        mainWindow.sequenceName = chr;
        mainWindow.sequence = sequence;
        mainWindow.sequenceStart = start;
        mainWindow.resultsInNewWindow = resultsInNewWindow;

        if (launchPoint.equals("roi")) {
        	mainWindow.windowDescriptionPane.setText("Running RNAStructure on the selected Region of Interest.\r\nTo run RNAStructure on the entire visible region, close this window, then click 'ScanFold' in the main IGV menu bar.");
        } else {
        	mainWindow.windowDescriptionPane.setText("Running RNAStructure on the visible region.\r\nTo run RNAStructure on an IGV 'Region of Interest', close this window, then right click the red 'Region of Interest' bar.");
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
							"-t", temperature.getText(),
							"rnastructure",
					}));
					
					if (resultsInNewWindow) {
						cmd.add("-j");
						cmd.add(new File(RNAStructureGui.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath());
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
