package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;

public class PbpSplitter {
	public static void splitFile(File inFile, File outFolder) throws IOException {
		if (!outFolder.exists()) {
			outFolder.mkdirs();
		}
		
		BufferedReader br = new BufferedReader(new FileReader(inFile));
		
		// burn off the header
		br.readLine();	
		
		String currGame = "";
		BufferedWriter currGameFile = null;
		
		while (true) {
			String line = br.readLine();
			if (line == null) {
				currGameFile.close();
				break;
			}
			
			Matcher m = PbpFile.lineParser.matcher(line);
			if (!m.matches()) {
				System.err.println(line);
				return;
			}
			
			String game = m.group(1);
			
			if (!currGame.equals(game)) {
				if (currGameFile != null) {
					currGameFile.close();
				}
				
				currGameFile = new BufferedWriter(new FileWriter(outFolder.getCanonicalPath() + "/" + game));
				currGame = game;
			}
			
			currGameFile.write(line + "\n");
		}
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Error: need two args!");
			return;			
		}
		
		try {
			splitFile(new File(args[0]), new File(args[1]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
