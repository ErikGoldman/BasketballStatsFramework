package plusminus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import parser.BballGeekFile;
import parser.DataSetRunner;
import parser.Roster;
import parser.RosterCalcState;
import data.Player;

public class ExperimentalPMData {
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
			return player + " time=(" + timeIn + "/" + timeOut + ") points=(" + pointsIn + "/" + pointsOut + ")";
		}
		
		public PMEntry(Player p) {
			this.player = p;
		}
		
		public double getPlusMinus() {
			return ((double)pointsIn / timeIn) - ((double)pointsOut / timeOut);
		}
	}
	
	public ExperimentalPMData(Roster rc) {
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
			String year = "2008";
			
			// compute the roster
			Roster rc = new Roster();
			DataSetRunner.run(new RosterCalcState(null, rc), year);
			
			// compute the +/- data
			ExperimentalPMData data = new ExperimentalPMData(rc); 			
			DataSetRunner.run(new ExperimentalPM(null, data), year);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// print out minutes
			while (true) {
				System.out.println("Team: ");
				String line = br.readLine();
				
				if (line == null) break;
				line = line.toUpperCase();
				
				ArrayList<PMEntry> players = data.getTeam(line);
				for (PMEntry p : players) {
					//System.out.println(p.player.getName() + ": " + p.getPlusMinus());
					System.out.println(p.player.getName() + ": " + p.pointsIn + " -" + p.pointsOut);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tickPoints(Player[] players, int startIndex, int points) {
		ArrayList<PMEntry> roster = teams.get(players[startIndex].getTeam());
		
		for (PMEntry e : roster) {
			boolean ticked = false;
			for (int i=startIndex; i < startIndex + 5; i++) {
				if (e.player == players[i]) {
					e.addPointsIn(points);
					ticked = true;
					break;
				}
			}
			if (!ticked)
				e.addPointsOut(points);
		}
	}
}
