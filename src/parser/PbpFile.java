package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Game;
import data.GameState;
import data.Play;

/*
 * +----------------+------------+---------------+------------------------------------------------------------------------------------------------------------------+
| GameID         | LineNumber | TimeRemaining | Entry                                                                                                            |
+----------------+------------+---------------+------------------------------------------------------------------------------------------------------------------+
| 20051101DALPHX |          1 | 00:48:00      |   Thomas Jump Ball Thomas vs Dampier                                                                             |
| 20051101DALPHX |          2 | 00:47:44      |  [DAL] Howard Turnover: Bad Pass (1 TO)  Steal: Marion (1 ST)                                                    |
| 20051101DALPHX |          3 | 00:47:31      |  [PHX 2-0] Thomas Layup Shot: Made (2 PTS)  Assist: Marion (1 AST)                                               |
| 20051101DALPHX |          4 | 00:47:12      |  [DAL 2-2] Dampier Layup Shot: Made (2 PTS)  Assist: Christie (1 AST)                                            |
| 20051101DALPHX |          5 | 00:47:07      |  [PHX 4-2] Marion Alley Oop Layup: Made (2 PTS)  Assist: Nash (1 AST)                                            |
| 20051101DALPHX |          6 | 00:46:54      |  [DAL] Nowitzki Jump Shot: Missed                                                                                |
| 20051101DALPHX |          7 | 00:46:52      |  [DAL] Howard Rebound (Off:1 Def:0)                                                                              |
| 20051101DALPHX |          8 | 00:46:40      |  [DAL] Terry Jump Shot: Missed                                                                                   |
| 20051101DALPHX |          9 | 00:46:38      |  [PHX] Bell Rebound (Off:0 Def:1)                                                                                |
| 20051101DALPHX |         10 | 00:46:28      |  [PHX] Marion Jump Shot: Missed                                                                                  |
 */
public class PbpFile {
	
	private static final String timeString = "(\\d+:\\d+:\\d+)";
	//private static final Pattern lineParser = Pattern.compile("\\| (\\d+\\w+)[\\s|]+(\\d+)[\\s|]+(-?[\\d:]+)[\\s|]+(.+)\\|");
	public static final Pattern lineParser = Pattern.compile("(\\d+\\w+)\\s+(\\d+)\\s+(-?\\d+:\\d+:\\d+)\\s+(.*)");
	
	private Game game;
	private ArrayList<Play> plays = new ArrayList<Play>();
	
	public PbpFile(File f) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(f));
		
		while (true) {
			String line = in.readLine();
			if (line == null)
				break;
			
			if (line.charAt(0) == '+') // comment line
				continue;
			
			Matcher m = lineParser.matcher(line);
			if (!m.matches()) {
				System.err.println("Uh oh, line didn't match: " + line);
				System.exit(1);
			}
			
			Play p = null;
			try {
				p = Play.parsePlay(m.group(1), m.group(2), m.group(3), m.group(4));
			} catch (Exception e) {
				System.err.println(f + " : " + m.group(4));
				e.printStackTrace();
			}
			
			if (p != null)
				plays.add(p);
		}
		
		Collections.sort(plays);		
		game = plays.get(0).getGame();
	}
	
	public int getNumPlays() {
		return plays.size();
	}
	
	public void apply(int play, GameState g) {
		plays.get(play).applyToGame(g);
	}
	
	public GameState getNewState() {
		return new GameState(game);
	}
	
	@Override
	public String toString() {
		return game.getId() + ": " + plays.size() + " plays";
	}
}
