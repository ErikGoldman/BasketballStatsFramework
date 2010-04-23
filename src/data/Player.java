package data;

import java.util.HashMap;

public class Player {
	private String team, name;
	private final int hash;
	
	private Player(String team, String name) {
		this.team = team;
		this.name = name;
		
		hash = (team + "|" + name).hashCode();
	}
	
	@Override
	public String toString() {
		return name + "(" + team + ")";
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTeam() {
		return team;
	}
	
	private static HashMap<String, Player> playerMap = new HashMap<String, Player>();
	
	public static Player getPlayer(String team, String name) {
		if (name == null || name.isEmpty())
			return null;
		
		String pId = team + "|" + name;
		
		if (!playerMap.containsKey(pId)) {
			playerMap.put(pId, new Player(team, name));
		}
		
		return playerMap.get(pId);
	}
}
