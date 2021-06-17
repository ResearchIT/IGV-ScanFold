package org.broad.igv.scanfold;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.broad.igv.feature.basepair.BasePairTrack;
import org.broad.igv.feature.genome.Genome;
import org.broad.igv.track.Track;
import org.broad.igv.feature.BasePairFileUtils;

import org.broad.igv.util.ResourceLocator;



public class ScanfoldlessDBNLoader {

	public static void loadFofnTwo(ResourceLocator inFile, List<Track> newTracks, Genome genome) throws
	FileNotFoundException, IOException {

        
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inFile.getPath()));
			String chromosome = br.readLine().trim();
			String strand = br.readLine().trim();
			int startPos = Integer.parseInt(br.readLine().trim());
			String dotBracketFile = br.readLine().trim();
			
			
			if (strand.equals("reverse")) {
				strand = "-";
			} else {
				strand = "+";
			}
			ResourceLocator dbLocator = new ResourceLocator(dotBracketFile);
			
	        String inPath = dbLocator.getPath();
	        String fileName = dbLocator.getFileName();
	        String outPath = inPath + ".bp";
	        
	        BasePairFileUtils.dotBracketToBasePairFile(inPath, outPath, chromosome, strand, startPos);
	        
	        ResourceLocator outFile = new ResourceLocator(outPath);
	        
	        String name = outFile.getTrackName();
	        String path = outFile.getPath();
	        String id = path + "_" + name;
	        newTracks.add(new BasePairTrack(outFile, id, name, genome));
	        
	        VarnaLoader.loadDotBracket(dotBracketFile, null);
	        
		} finally {
			if (br != null) br.close();
		}
	}
	
}
