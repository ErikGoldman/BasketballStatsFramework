package PlusMinus;

import java.util.HashMap;

public class Player {
	private int time, points;
	
	private String team, name;
	private Player(String team, String name) {
		this.team = team;
		this.name = name;
		
		this.time = 0;
		this.points = 0;
	}
	
	public void addTime(int time) {
		this.time += time;
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
	
	@Override
	public String toString() {
		return name + "(" + team + ")";
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
