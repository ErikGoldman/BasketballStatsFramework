package parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import data.Player;

public class RosterCalculator {
	protected HashMap<String, HashSet<Player>> roster = new HashMap<String, HashSet<Player>>();
	
	public RosterCalculator(File folder) throws IOException {
		for (File f : folder.listFiles()) {
			if (!f.getName().endsWith(".csv")) continue;
			
			BballGeekFile gf = new BballGeekFile(f);
			RosterCalcState gs = new RosterCalcState(gf.getGame(), this);
			gf.apply(gs);
		}
	}
	
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
