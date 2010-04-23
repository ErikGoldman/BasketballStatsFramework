package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import data.TimeChangePlay;
import data.FoulPlay;
import data.FreeThrowPlay;
import data.Game;
import data.GameState;
import data.Play;
import data.Player;
import data.ReboundPlay;
import data.ShotPlay;
import data.SubPlay;

public class BballGeekFile {
	private static final String HEADER_LINE = "a1,a2,a3,a4,a5,h1,h2,h3,h4,h5,period,time,team,etype,assist,away,block,entered,home,left,num,opponent,outof,player,points,possession,reason,result,steal,type,x,y",
								SHOT_TYPE = "shot",
								REBOUND_TYPE = "rebound",
								FOUL_TYPE = "foul",
								FREE_THROW_TYPE = "free throw",
								SUB_TYPE = "sub",
								JUMP_BALL_TYPE = "jump ball",
								OFFENSIVE_REBOUND = "off";
	
	private static final int
					PERIOD_INDEX = 10,
					TIME_INDEX   = 11,
					TEAM_INDEX   = 12,
					PLAY_TYPE_INDEX   = 13,
					ASSIST_INDEX = 14,
					SUB_ENTERED_INDEX = 19,
					SUB_LEFT_INDEX = 19,
					FT_NUM_INDEX = 20,
					OPPONENT_INDEX = 21,
					PLAYER_INDEX = 23,
					POINTS_INDEX = 24,
					REASON_INDEX = 26,
					RESULT_INDEX = 27,
					TYPE_INDEX = 29,
					X_INDEX = 30,
					Y_INDEX = 31;
	
	private ArrayList<Play> plays = new ArrayList<Play>();
	private Game game;
	
	public BballGeekFile(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		// read the header line
		String line = br.readLine();
		if (!line.equals(HEADER_LINE))
			throw new IOException("Unrecognized header: " + line);
		
		game = Game.getGame(f.getName());
		
		int currPeriod = 1;
		Player[] lastPlayers = null;
		
		while (true) {
			String id = f.getName();
			
			line = br.readLine();
			if (line == null) {
				plays.add(new TimeChangePlay(id, getTimeForPeriod(currPeriod), lastPlayers));
				break;
			}
			
			String[] parts = mySplit(line);
			Player[] activePlayers = new Player[10];
			for (int i=0; i < 5; i++) {
				activePlayers[i] = Player.getPlayer(game.getAwayTeam(), parts[i]);
			}
			for (int i=5; i < 10; i++) {
				activePlayers[i] = Player.getPlayer(game.getHomeTeam(), parts[i]);
			}
			
			int period = Integer.parseInt(parts[PERIOD_INDEX]);
			if (currPeriod != period) {
				plays.add(new TimeChangePlay(id, getTimeForPeriod(currPeriod), lastPlayers));
				currPeriod = period;
			}
			
			try {
				int time = makeTime(Integer.parseInt(parts[PERIOD_INDEX]), parts[TIME_INDEX]);
				
				// shot
				if (parts[PLAY_TYPE_INDEX].equals(SHOT_TYPE)) {
					Player scorer = Player.getPlayer(parts[TEAM_INDEX], parts[PLAYER_INDEX]),
						   assist = null;
					
					if (!parts[ASSIST_INDEX].isEmpty()) {
						assist = Player.getPlayer(parts[TEAM_INDEX], parts[ASSIST_INDEX]);
					}
					
					int points = 0;
					if (!parts[POINTS_INDEX].isEmpty())
						points = Integer.parseInt(parts[POINTS_INDEX]);
					
					String type = parts[TYPE_INDEX];
					boolean made = true;
					if (parts[RESULT_INDEX].equals("missed"))
						made = false;
					
					int x=-1, y=-1;
					
					if (!parts[X_INDEX].isEmpty()) {
						x = Integer.parseInt(parts[X_INDEX]); 
					    y = Integer.parseInt(parts[Y_INDEX]);
					}
					
					plays.add(new ShotPlay(id, time, activePlayers, scorer, type, points, made, assist, x, y));
				} else if (parts[PLAY_TYPE_INDEX].equals(REBOUND_TYPE)) {
					Player player = Player.getPlayer(parts[TEAM_INDEX], parts[PLAYER_INDEX]);
					String type = parts[PLAY_TYPE_INDEX];
					
					plays.add(new ReboundPlay(id, time, activePlayers, player, type.equals(OFFENSIVE_REBOUND)));
				} else if (parts[PLAY_TYPE_INDEX].equals(FOUL_TYPE)) {
					Player fouler = Player.getPlayer(parts[TEAM_INDEX], parts[PLAYER_INDEX]),
						   fouled = Player.getPlayer(game.getOtherTeam(parts[TEAM_INDEX]), parts[OPPONENT_INDEX]);
					
					String type = parts[TYPE_INDEX];
					
					plays.add(new FoulPlay(id, time, activePlayers, fouler, fouled, type));
				} else if (parts[PLAY_TYPE_INDEX].equals(FREE_THROW_TYPE)) {
					Player scorer = Player.getPlayer(parts[TEAM_INDEX], parts[PLAYER_INDEX]);
					boolean made = true;
					if (parts[RESULT_INDEX].equals("missed"))
						made = false;
					
					int num = Integer.parseInt(parts[FT_NUM_INDEX]);
					
					plays.add(new FreeThrowPlay(id, time, activePlayers, scorer, num, made));
				} else if(parts[PLAY_TYPE_INDEX].equals(SUB_TYPE)) {
					Player outPlayer = Player.getPlayer(parts[TEAM_INDEX], parts[SUB_LEFT_INDEX]),
						    inPlayer = Player.getPlayer(parts[TEAM_INDEX], parts[SUB_ENTERED_INDEX]);
					
					plays.add(new SubPlay(id, time, activePlayers, inPlayer, outPlayer));
				} else if(parts[PLAY_TYPE_INDEX].equals(JUMP_BALL_TYPE)) {
					plays.add(new TimeChangePlay(id, 0, activePlayers));
				} else {
					//System.err.println(parts[PLAY_TYPE_INDEX]);
				}
				
				lastPlayers = activePlayers;
			
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.err.println(line);
			}
		}
	}
	
	private String[] mySplit(String line) {
		String[] out = new String[32];
		
		int lastStart = 0;
		for (int i=0; i < out.length-1; i++) {
			int comma = line.indexOf(',', lastStart);
			
			out[i] = line.substring(lastStart, comma);
			lastStart = comma+1;
		}
		
		out[out.length-1] = line.substring(lastStart);
		
		return out;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void apply(GameState gs) {
		for (Play p : plays)
			p.applyToGame(gs);
	}
	
	public ArrayList<Play> getPlays() {
		return plays;
	}
	
	private int getTimeForPeriod(int period) {
		return Math.min(period, 4) * 12 * 60 + Math.max(period - 4, 0) * 5 * 60; 		
	}
	
	private int makeTime(int period, String time) {
		String[] parts = time.split(":");
		
		int timeElapsed = getTimeForPeriod(period); 
		
		return timeElapsed - (Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]));
	}
}
