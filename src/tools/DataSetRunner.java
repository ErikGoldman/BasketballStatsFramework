package tools;

import java.io.File;
import java.io.IOException;

import data.BballGeekFile;
import data.GameState;

public class DataSetRunner {

	public static void run(GameState templateState, String year) {
		File folder = new File("./data/" + year);
		for (File f : folder.listFiles()) {
			if (!f.getName().endsWith(".csv")) continue;
			
			try {
				BballGeekFile gf = new BballGeekFile(f);
				GameState gs = templateState.createCopy(gf.getGame());			
				gf.apply(gs);
			} catch (IOException e) {
				System.err.println("Could not run on file " + f);
				e.printStackTrace();
			}
		}
	}
	
	public static void run(GameState templateState, String[] years) {
		for (String year : years) {
			run(templateState, year);
		}
	}
}
