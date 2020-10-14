package org.broad.igv.scanfold;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JDialog;


public class VarnaLoader {
	
	// based on org.broad.igv.feature.BasePairFileUtils::loadDotBracket
	public static void loadDotBracket(String inFile) throws
	FileNotFoundException, IOException {
		// TODO: add error messages for misformatted file

		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(inFile));
			
			String title = "";
			String sequence = "";
			String struct = "";

			String nextLine;
			while ((nextLine = br.readLine()) != null) {

				// skip comment lines if present
				if (nextLine.startsWith("#")) {
					continue;
				}
				
				// skip leading header lines if present
				if (nextLine.startsWith(">")) {
					if (sequence.length() > 0 && struct.length() > 0) {
						makeVarnaPopup(title, sequence, struct);
					}
					title = nextLine;
					sequence = "";
					struct = "";
					continue;
				}

				String s = nextLine.trim();
				if (s.chars().allMatch(Character::isLetter)) {
					// sequence line
					sequence += s;
				} else {
					// assumed structure line
					struct += s;
				}
			}
			
			if (sequence.length() > 0 && struct.length() > 0) {
				makeVarnaPopup(title, sequence, struct);
			}
			
		} finally {
			if (br != null) br.close();
		}

	}

    
	public static void makeVarnaPopup(String windowTitle, String sequence, String structure) {
		try {
			VarnaPopup dialog = new VarnaPopup(windowTitle, sequence, structure);
			//dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
