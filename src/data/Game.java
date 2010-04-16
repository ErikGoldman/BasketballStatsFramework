package data;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {
	private Pattern idSplitter = Pattern.compile("(\\d+)(\\w\\w\\w)(\\w\\w\\w)");
	
	// format: 20051101DALPHX
	private Game(String id) {
		this.gameId = id;
		
		Matcher m = idSplitter.matcher(id);
		if (!m.matches()) {
			throw new RuntimeException("Can't match game id " + id);
		}
		
		this.homeTeam = m.group(2).intern();
		this.awayTeam = m.group(3).intern();
	}
	
	private static HashMap<String, Game> gameMap = new HashMap<String, Game>();
	public static Game getGame(String id) {
		if (!gameMap.containsKey(id)) {
			gameMap.put(id, new Game(id));
		}
		
		return gameMap.get(id);
	}
	
	public String getId() {
		return gameId;
	}
	
	@Override
	public String toString() {
		return gameId;
	}
	
	public boolean isHome(String team) {
		return team.equals(homeTeam);
	}
	public boolean isAway(String team) {
		return team.equals(awayTeam);
	}
	
	public String getHomeTeam() {
		return homeTeam;
	}
	
	public String getAwayTeam() {
		return awayTeam;
	}
	
	private String gameId, homeTeam, awayTeam;
}
