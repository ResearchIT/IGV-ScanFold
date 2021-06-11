package org.broad.igv.scanfold;

import java.awt.Dialog.ModalityType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.IntStream;

import javax.swing.JDialog;

import org.broad.igv.data.WiggleDataset;
import org.broad.igv.data.WiggleParser;
import org.broad.igv.feature.genome.Genome;
import org.broad.igv.util.ResourceLocator;


public class VarnaLoader {
	
	public static void loadFofn(ResourceLocator inFile, Genome genome) throws
	FileNotFoundException, IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inFile.getPath()));
			String strand = br.readLine().trim();
			String dotBracketFile = br.readLine().trim();
			String colorFile = br.readLine();
			Double[] colors;
			if (colorFile != null) {
				colorFile = colorFile.trim();
				colors = loadColorFile(colorFile, genome, strand);
			} else {
				colors = null;
			}
			loadDotBracket(dotBracketFile, colors);
		} finally {
			if (br != null) br.close();
		}
	}
	
	public static Double[] loadColorFile(String inFile, Genome genome, String strand) {
		WiggleDataset ds = (new WiggleParser(new ResourceLocator(inFile), genome)).parse();
		float[] theData = ds.getData("",ds.getChromosomes()[0]);
		Double[] colors = IntStream.range(0, theData.length).mapToDouble(i -> theData[i]).boxed().toArray(Double[]::new);
		if (strand.equals("reverse")) {
			for(int i = 0; i < colors.length / 2; i++) {
			    Double tmp = colors[i];
			    colors[i] = colors[colors.length - i - 1];
			    colors[colors.length - i - 1] = tmp;
			}
		}
		return colors;
	}
	// based on org.broad.igv.feature.BasePairFileUtils::loadDotBracket
	public static void loadDotBracket(String inFile, Double[] colors) throws
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
						makeVarnaPopup(title, sequence, struct, colors);
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
				makeVarnaPopup(title, sequence, struct, colors);
			}
			
		} finally {
			if (br != null) br.close();
		}

	}

    
	public static void makeVarnaPopup(String windowTitle, String sequence, String structure, Double[] colors) {
		try {
			VarnaPopup dialog = new VarnaPopup(windowTitle, sequence, structure, colors);
			//dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
