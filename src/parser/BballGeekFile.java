package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import data.Play;
import data.Player;
import data.ShotPlay;

public class BballGeekFile {
	private static final String HEADER_LINE = "a1,a2,a3,a4,a5,h1,h2,h3,h4,h5,period,time,team,etype,assist,away,block,entered,home,left,num,opponent,outof,player,points,possession,reason,result,steal,type,x,y",
								SHOT_TYPE = "shot";
	
	private static final int
					PERIOD_INDEX = 10,
					TIME_INDEX   = 11,
					TEAM_INDEX   = 12,
					TYPE_INDEX   = 13,
					PLAYER_INDEX = 23,
					POINTS_INDEX = 24,
					REASON_INDEX = 26,
					RESULT_INDEX = 27,
					SHOT_TYPE_INDEX = 29,
					X_INDEX = 30,
					Y_INDEX = 31;
	
	private ArrayList<Play> plays = new ArrayList<Play>();
	
	public BballGeekFile(File f) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		// read the header line
		String line = br.readLine();
		if (!line.equals(HEADER_LINE))
			throw new Exception("Unrecognized header: " + line);
		
		while (true) {
			line = br.readLine();
			if (line == null)
				break;
			
			String[] parts = line.split(",");
			
			int time = makeTime(Integer.parseInt(parts[PERIOD_INDEX]), parts[TIME_INDEX]);
			String id = f.getName();
			
			
			// shot
			if (parts[TYPE_INDEX].equals(SHOT_TYPE)) {
				Player scorer = Player.getPlayer(parts[TEAM_INDEX], parts[PLAYER_INDEX]);
				
				int points = 0;
				if (!parts[POINTS_INDEX].isEmpty())
					points = Integer.parseInt(parts[POINTS_INDEX]);
				
				String type = parts[TYPE_INDEX];
				boolean made = true;
				if (parts[RESULT_INDEX].equals("missed"))
					made = false;
				
				int x = Integer.parseInt(parts[X_INDEX]), 
				    y = Integer.parseInt(parts[Y_INDEX]);
				
				plays.add(new ShotPlay(id, time, scorer, type, points, made));
			}
		}
	}
	
	private int makeTime(int period, String time) {
		String[] parts = time.split(":");
		
		int timeElapsed = Math.min(period, 4) * 12 * 60 + Math.max(period - 4, 0) * 5 * 60; 
		
		return timeElapsed - (Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]));
	}
}
