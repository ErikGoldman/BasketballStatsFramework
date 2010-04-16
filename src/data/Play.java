package data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Play implements Comparable<Play> {
	private static final String shotTypes = 
					"(pullup jump|slam dunk|driving layup|jump|3pt|" + 
					"driving reverse layup|alley oop layup|layup|turnaround fade away|" +
					"dunk|reverse layup|step back jump|tip)",
			regBox =   "\\[(\\w+)\\] ",
			scoreBox = "\\[(\\w+) (\\d+)-(\\d+)\\] ",
			someBox = "(" + regBox + "|" + scoreBox + ")",
			playerStr = "([\\w\\s\\.]+?)";
	
	private static final Pattern 
		subPattern = Pattern.compile(regBox + playerStr + " Substitution replaced by " + playerStr, Pattern.CASE_INSENSITIVE),
		scorePattern = Pattern.compile(someBox + playerStr + " " + shotTypes + " shot: (made|missed).*", Pattern.CASE_INSENSITIVE),
		freeThrowPattern = Pattern.compile(someBox + playerStr + " free throw (\\d) of (\\d) (missed)?.*", Pattern.CASE_INSENSITIVE),
		reboundPattern = Pattern.compile(regBox + playerStr + " Rebound \\(Off:(\\d+) Def:(\\d+)\\)", Pattern.CASE_INSENSITIVE);
	
	private Game game;
	private int timeLeft, lineNum;
	
	public Game getGame() {
		return game;
	}
	
	protected Play(String id, String lineNumStr, String time) {
		game = Game.getGame(id);
		lineNum = Integer.parseInt(lineNumStr);
		parseTime(time);
	}
		
	private void parseTime(String t) {
		String[] components = t.split(":");
		
		timeLeft = Integer.parseInt(components[0]) * (60 * 60) + Integer.parseInt(components[1]) * 60
						+ Integer.parseInt(components[2]);
	}
	
	@Override
	public String toString() {
		return game + "|" + timeLeft + "|" + lineNum;
	}
	
	protected abstract void apply(GameState g);
	
	public static Play parsePlay(String id, String lineNumStr, String time, String play) {
		// substitution
		Matcher subM = subPattern.matcher(play);
		if (subM.matches()) {
			String team = subM.group(1);
			
			Player outPlayer = Player.getPlayer(team, subM.group(2)),
				   inPlayer  = Player.getPlayer(team, subM.group(3));
			
			return new SubPlay(id, lineNumStr, time, inPlayer, outPlayer);
		}
		
		// scoring play
		Matcher scoreM = scorePattern.matcher(play);
		if (scoreM.matches()) {			
			String team;
			int teamScore = -1;
			String shotType = scoreM.group(7);
			
			boolean made = true;
			// matched a missed at the end there
			if (scoreM.group(scoreM.groupCount()).equalsIgnoreCase("missed")) {
				made = false;
				team = scoreM.group(2);
			} else {
				team = scoreM.group(3);
				teamScore = Integer.parseInt(scoreM.group(4));
			}
		
			Player scorer = Player.getPlayer(team, scoreM.group(6));
			String type = scoreM.group(7);
			
			return new ShotPlay(id, lineNumStr, time, scorer, type, teamScore, made);
		}
		
		// free throw
		Matcher freeThrowM = freeThrowPattern.matcher(play);
		if (freeThrowM.matches()) {
			String team;
			int teamScore = -1;
			
			boolean made = true;
			// matched a missed at the end there
			if (freeThrowM.group(freeThrowM.groupCount()) != null) {
				made = false;
				team = freeThrowM.group(2);
			} else {
				team = freeThrowM.group(3);
				teamScore = Integer.parseInt(freeThrowM.group(4));
			}
		
			Player scorer = Player.getPlayer(team, freeThrowM.group(6));
			
			return new FreeThrowPlay(id, lineNumStr, time, scorer, teamScore, made);
		}
		
		// rebound
		Matcher reboundM = reboundPattern.matcher(play);
		if (reboundM.matches()) {
			String team = reboundM.group(1);
			Player player = Player.getPlayer(team, reboundM.group(2));
			
			int offRebound = Integer.parseInt(reboundM.group(3)),
				defRebound = Integer.parseInt(reboundM.group(4));
			
			return new ReboundPlay(id, lineNumStr, time, player, offRebound, defRebound);
		}
		
		System.err.println("Unrecognized: " + play);
		return null;
	}
	
	public void applyToGame(GameState g) {
		g.updateTime(timeLeft);
		apply(g);
	}
	
	@Override
	public int compareTo(Play other) {
		return lineNum - other.lineNum; 
	}
}
