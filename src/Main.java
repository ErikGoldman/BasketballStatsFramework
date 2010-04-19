import java.io.File;

import parser.BballGeekFile;


public class Main {
	public static void main(String[] args) {
		try {
			BballGeekFile f = new BballGeekFile(new File ("data/2008_geek_pbp/20081223.MEMDAL.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
