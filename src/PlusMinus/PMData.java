package PlusMinus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import parser.BballGeekFile;
import parser.RosterCalculator;
import data.Player;

public class PMData {
	private class PMEntry {
		Player player;
		int timeIn = 0, timeOut = 0;	
		int pointsIn = 0, pointsOut = 0;
		
		
		
		public void addPointsIn(int p) {
			pointsIn += p;
		}
		public void addPointsOut(int p) {
			pointsOut += p;
		}
		
		public void addTimeIn(int t) {
			timeIn += t;
		}
		public void addTimeOut(int t) {
			timeOut += t;
		}
		
		public String toString() {
			return player + " (" + timeIn + "/" + timeOut + ")";
		}
		
		public PMEntry(Player p) {
			this.player = p;
		}
	}
	
	public PMData(RosterCalculator rc) {
		for (String team : rc.getRoster().keySet()) {
			ArrayList<PMEntry> teamList = new ArrayList<PMEntry>();
			for (Player p : rc.getPlayers(team)) {
				teamList.add(new PMEntry(p));
			}
			teams.put(team, teamList);
		}		
	}
	
	HashMap<String, ArrayList<PMEntry>> teams = new HashMap<String, ArrayList<PMEntry>>();
	
	public void tick(Player[] active, int time) {
		ArrayList<PMEntry> away = teams.get(active[0].getTeam());
		for (PMEntry e : away) {
			boolean ticked = false;
			for (int i=0; i < 5; i++) {
				if (e.player == active[i]) {
					e.addTimeIn(time);
					ticked = true;
					break;
				}
			}
			if (!ticked)
				e.addTimeOut(time);
		}
		
		ArrayList<PMEntry> home = teams.get(active[5].getTeam());
		for (PMEntry e : home) {
			boolean ticked = false;
			for (int i=5; i < 10; i++) {
				if (e.player == active[i]) {
					e.addTimeIn(time);
					ticked = true;
					break;
				}
			}
			if (!ticked)
				e.addTimeOut(time);
		}
	}
	
	public ArrayList<PMEntry> getTeam(String team) {
		return teams.get(team);
	}
	
	public static void main(String[] args) {
		try {
			File folder = new File("data/2008_geek_pbp");
			
			RosterCalculator rc = new RosterCalculator(folder);
			PMData data = new PMData(rc); 
			
			for (File f : folder.listFiles()) {
				if (!f.getName().endsWith(".csv")) continue;
				
				BballGeekFile gf = new BballGeekFile(f);
				PMCalc pmc = new PMCalc(gf.getGame(), data);
				gf.apply(pmc);
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// print out minutes
			while (true) {
				System.out.println("Team: ");
				String line = br.readLine();
				
				if (line == null) break;
				line = line.toUpperCase();
				
				ArrayList<PMEntry> players = data.getTeam(line);
				for (PMEntry p : players) {
					System.out.println(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
