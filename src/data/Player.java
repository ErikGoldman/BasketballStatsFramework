package data;

import java.util.HashMap;

public class Player {
	private String team, name;
	private Player(String team, String name) {
		this.team = team;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + "(" + team + ")";
	}
	
	public String getName() {
		return name;
	}
	
	public String getTeam() {
		return team;
	}
	
	private static HashMap<String, Player> playerMap = new HashMap<String, Player>();
	
	public static Player getPlayer(String team, String name) {
		String pId = team + "|" + name;
		
		if (!playerMap.containsKey(pId)) {
			playerMap.put(pId, new Player(team, name));
		}
		
		return playerMap.get(pId);
	}
}
