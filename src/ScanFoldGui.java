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
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import fr.orsay.lri.varna.VARNAPanel;
import fr.orsay.lri.varna.exceptions.ExceptionNonEqualLength;

public class ScanFoldGui extends JDialog {

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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ScanFoldGui dialog = new ScanFoldGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ScanFoldGui() {
		setTitle("scanfoldgui");
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 35, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		contentPanel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_contentPanel = new GridBagConstraints();
		gbc_contentPanel.fill = GridBagConstraints.BOTH;
		gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contentPanel.gridx = 0;
		gbc_contentPanel.gridy = 0;
		getContentPane().add(contentPanel, gbc_contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{119, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{38, 35, 47, 41, 34, 0, 23, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
			gbc_windowSize.insets = new Insets(0, 0, 5, 0);
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
			gbc_stepSize.insets = new Insets(0, 0, 5, 0);
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
			gbc_randomizations.insets = new Insets(0, 0, 5, 0);
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
			gbc_shuffleType.insets = new Insets(0, 0, 5, 0);
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
			gbc_temperature.insets = new Insets(0, 0, 5, 0);
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
			competition.setText("0");
			GridBagConstraints gbc_competition = new GridBagConstraints();
			gbc_competition.insets = new Insets(0, 0, 5, 0);
			gbc_competition.fill = GridBagConstraints.HORIZONTAL;
			gbc_competition.gridx = 1;
			gbc_competition.gridy = 5;
			contentPanel.add(competition, gbc_competition);
		}
		{
			JCheckBox chckbxNewCheckBox = new JCheckBox("Global Refold");
			chckbxNewCheckBox.setToolTipText("When checked, this option will refold your entire sequence while constraining the most significant base pairs (with Zavg values < -1 and < -2).\n");
			GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
			gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxNewCheckBox.anchor = GridBagConstraints.WEST;
			gbc_chckbxNewCheckBox.gridx = 0;
			gbc_chckbxNewCheckBox.gridy = 6;
			contentPanel.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Log Output", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 1;
			getContentPane().add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]{3, 0};
			gbl_panel.rowHeights = new int[]{3, 0};
			gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			panel.setLayout(gbl_panel);
			{
				JScrollPane scrollPane = new JScrollPane();
				GridBagConstraints gbc_scrollPane = new GridBagConstraints();
				gbc_scrollPane.anchor = GridBagConstraints.NORTHWEST;
				gbc_scrollPane.gridx = 0;
				gbc_scrollPane.gridy = 0;
				panel.add(scrollPane, gbc_scrollPane);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 2;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public ScanFoldGui(boolean isVarna) {
		setTitle("scanfoldgui");
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 35, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			try {
				JPanel panel = new fr.orsay.lri.varna.VARNAPanel("CCCCAUAUGGGGACC", "((((....))))...");
				GridBagConstraints gbc_panel = new GridBagConstraints();
				gbc_panel.insets = new Insets(0, 0, 5, 0);
				gbc_panel.fill = GridBagConstraints.BOTH;
				gbc_panel.gridx = 0;
				gbc_panel.gridy = 1;
				getContentPane().add(panel, gbc_panel);
			} catch (ExceptionNonEqualLength e) {
				e.printStackTrace();
			}
		}
	}
	
    public static void launch(boolean modal, String genomeId) {
        ScanFoldGui mainWindow = new ScanFoldGui(true);
        mainWindow.pack();
        mainWindow.setModal(modal);
        mainWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainWindow.setResizable(true);

//        if (genomeId != null) {
//            mainWindow.genomeField.setText(genomeId);
//            mainWindow.genomeField.setEnabled(false);
//            mainWindow.genomeField.setToolTipText("<html>To change the genome id close this window and <br>use the pulldown on the IGV batch screen.");
//            mainWindow.genomeButton.setEnabled(false);
//            mainWindow.genomeSelectionDisabled = true;
//        }

        mainWindow.setVisible(true);
    }

}
