package example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import tools.DataSetRunner;
import tools.Roster;
import tools.RosterCalcState;
import data.Player;

public class PMData {
	
	// a class that represents plus/minus data for a single player
	private class PMEntry {
		Player player;
		int timeIn = 0;	
		int pointsPlus = 0, pointsMinus = 0;
		
		public void addPoints(int p) {
			if (p > 0)
				pointsPlus += p;
			else
				pointsMinus -= p;
		}
		
		public void addTimeIn(int t) {
			timeIn += t;
		}
		
		public String toString() {
			return player + " time=" + timeIn+ " points=" + pointsPlus + " " + pointsMinus;
		}
		
		public PMEntry(Player p) {
			this.player = p;
		}
		
		public double getPlusMinus() {
			return pointsPlus - pointsMinus;
		}
		
		public double getPlusMinusPer48() {
			return (((double)(pointsPlus - pointsMinus)) / timeIn) * 48 * 60;
		}
	}
	
	HashMap<Player, PMEntry> players = new HashMap<Player, PMEntry>();
	
	public void tick(Player[] active, int time) {
		for (Player p : active) {
			getPlayer(p).addTimeIn(time);
		}
	}
	
	public void tickPoints(Player[] players, int startIndex, int points) {
		for (int i=startIndex; i < startIndex + 5; i++) {
			getPlayer(players[i]).addPoints(points);
		}
	}
	
	public PMEntry getPlayer(Player player) {
		PMEntry e = players.get(player);
		if (e == null) {
			e = new PMEntry(player);
			players.put(player, e);
		}
		
		return e;
	}
	
	public static void main(String[] args) {
		try {
			String year = "2009";
			
			// compute a roster so we can search by team
			Roster rc = new Roster();
			DataSetRunner.run(new RosterCalcState(null, rc), year);
			
			// compute the individual +/- data for each player
			PMData data = new PMData(); 			
			DataSetRunner.run(new PMCalc(null, data), year);

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			// print out minutes
			while (true) {
				System.out.println("Enter a 3-letter team name (like CLE or LAL): ");
				String line = br.readLine();
				
				if (line == null) break;
				line = line.toUpperCase().trim();
				
				System.out.println("name\t+/-\t+/- per 48");
				
				HashSet<Player> players = rc.getPlayers(line);
				for (Player p : players) {
					PMEntry pm = data.getPlayer(p);
					
					System.out.println(p.getName() + "\t" + pm.getPlusMinus() + "\t" + pm.getPlusMinusPer48());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
