package PlusMinus;

import java.util.List;

import data.Play;

public class PMGame {
	private Player[][] units = new Player[2][5];
	private String home, away;
	private int lastTime;
	
	private List<Play> plays;
	
	public PMGame(String home, String away, List<Play> plays) {
		this.home = home;
		this.away = away;
		
		this.lastTime = 48 * 60; // start @ 48 min
		
		this.plays = plays;
		
		getStartingLineups();
	}
	
	public void updateTime(int timeLeft) {
		for (Player[] u : units) {
			for (Player p : u) {
				p.addTime(lastTime - timeLeft);
			}
		}
		
		lastTime = timeLeft;
	}
	
	public void substitute(String team, Player pIn, Player pOut) {
		int index = 0;
		if (away.equals(team)) {
			index = 1;
		}
		
		for (int i=0; i < units[index].length; i++) {
			if (units[index][i] == pIn) {
				units[index][i] = pOut;
				return;
			}
		}
		
		// ok, the other player wasn't found.
	}
	
	public void getStartingLineups() {
		Player[][] starting = new Player[2][5];
		for (Play p : plays) {
		}
	}
}
