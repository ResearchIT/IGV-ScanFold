package org.broad.igv.scanfold;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.broad.igv.ui.IGV;

import fr.orsay.lri.varna.models.rna.RNA;
import fr.orsay.lri.varna.VARNAPanel;
import fr.orsay.lri.varna.exceptions.ExceptionNonEqualLength;

public class VarnaPopup extends JDialog {
	public VarnaPopup(String windowTitle, String sequence, String structure, Double[] colors) {
		//super(IGV.getMainFrame(), true);
		setTitle(windowTitle);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 35, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			try {
				fr.orsay.lri.varna.VARNAPanel panel = new fr.orsay.lri.varna.VARNAPanel(sequence, structure, RNA.DRAW_MODE_NAVIEW);
				panel.setColorMapValues(colors);
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

}
