package parser;

import java.util.HashMap;
import java.util.HashSet;

import data.Player;

public class Roster {
	protected HashMap<String, HashSet<Player>> roster = new HashMap<String, HashSet<Player>>();
	
	public void addPlayer(Player p) {
		HashSet<Player> list = roster.get(p.getTeam());
		if (list == null) {
			list = new HashSet<Player>();
			roster.put(p.getTeam(), list);
		}
		
		list.add(p);
	}
	
	public HashSet<Player> getPlayers(String team) {
		return roster.get(team);
	}
	
	public HashMap<String, HashSet<Player>> getRoster() {
		return roster;
	}
}
