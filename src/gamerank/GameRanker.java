package gamerank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import parser.PbpFile;
import data.GameState;

public class GameRanker {
	
	private static final int MAX_ITERATIONS = 1000;
	private static final double TOLERANCE = 1E-20;

	private class TeamDoublePair implements Comparable<TeamDoublePair>{
		String otherTeam;
		double scoreMargin;
		
		public TeamDoublePair(String t, double s) {
			otherTeam = t;
			scoreMargin = s;
		}
		
		@Override
		public int compareTo(TeamDoublePair other) {
			if (scoreMargin > other.scoreMargin) return 1;
			if (scoreMargin < other.scoreMargin) return -1;
			return 0;
		}
	}
	
	private HashMap<String, ArrayList<TeamDoublePair>> scores = new HashMap<String, ArrayList<TeamDoublePair>>();
	
	public GameRanker(File folder) throws IOException {
		for (File f : folder.listFiles()) {
			PbpFile pbp = new PbpFile(f);
			GameState g = pbp.getNewState();
			for (int i=0; i < pbp.getNumPlays(); i++) {
				pbp.apply(i, g);
			}
			
			int scoreMargin;
			String losingTeam, winningTeam;
			if (g.getAwayScore() > g.getHomeScore()) {
				scoreMargin = g.getAwayScore() - g.getHomeScore();
				losingTeam = g.getGame().getHomeTeam();
				winningTeam = g.getGame().getAwayTeam();
			} else {
				scoreMargin = g.getHomeScore() - g.getAwayScore();
				losingTeam = g.getGame().getAwayTeam();
				winningTeam = g.getGame().getHomeTeam();
			}
			
			if (scores.get(losingTeam) == null) {
				scores.put(losingTeam, new ArrayList<TeamDoublePair>());
			}
			scores.get(losingTeam).add(new TeamDoublePair(winningTeam, scoreMargin));			
		}
	}
	
	public TeamDoublePair[] computeRank() {
		HashMap<String, Double> rankVals = new HashMap<String, Double>();
		for (String team : scores.keySet()) {
			rankVals.put(team, 1.0 / scores.keySet().size());
		}
		
		// normalize loss values
		normalizeNoScore();
		//normalizeWithLogScore();
		
		// invert our links so we have team --> all games they won
		HashMap<String, ArrayList<TeamDoublePair>> inLinks = new HashMap<String, ArrayList<TeamDoublePair>>();
		for (String team : scores.keySet()) {
			for (TeamDoublePair gs : scores.get(team)) {
				if (inLinks.get(gs.otherTeam) == null) {
					inLinks.put(gs.otherTeam, new ArrayList<TeamDoublePair>());
				}
				
				inLinks.get(gs.otherTeam).add(new TeamDoublePair(team, gs.scoreMargin));
			}
		}
		
		for (int i=0; i < MAX_ITERATIONS; i++) {
			System.out.print("Iteration " + i + ": ");
			System.out.flush();
			
			double diff = 0.0, total = 0.0;
			
			// compute page rank
			for (String team : inLinks.keySet()) {
				total += rankVals.get(team);
				
				double newRank = 0.0;
				for (TeamDoublePair gs : inLinks.get(team)) {
					newRank += gs.scoreMargin * rankVals.get(gs.otherTeam);
				}
				
				diff += Math.abs(newRank - rankVals.get(team));
				
				rankVals.put(team, newRank);
			}
			System.out.println(diff + ", total = " + total);
			
			if (diff <= TOLERANCE)
				break;
		}
		
		TeamDoublePair[] out = new TeamDoublePair[rankVals.size()];
		
		int i=0;
		for (String team : rankVals.keySet()) {
			out[i++] = new TeamDoublePair(team, -rankVals.get(team)); // put a negative in so it sorts backwards
		}
		
		Arrays.sort(out);
		
		System.out.println("Done!");
		
		return out;
	}
	
	private void normalizeNoScore() {
		for (String team : scores.keySet()) {
			double totalLosses = 0;
			for (TeamDoublePair gs : scores.get(team)) {
				if (gs.scoreMargin > 0) {
					totalLosses += 1.0;
				}
			}
			
			for (TeamDoublePair gs : scores.get(team)) {
				if (gs.scoreMargin > 0) {
					gs.scoreMargin = 1.0 / totalLosses;
				} else {
					gs.scoreMargin = 0.0;
				}
			}
		}
	}
	
	private void normalizeWithLogScore() {
		for (String team : scores.keySet()) {
			double totalPoints = 0;
			for (TeamDoublePair gs : scores.get(team)) {
				if (gs.scoreMargin > 0) {
					totalPoints += Math.log(gs.scoreMargin + 1);
				}
			}
			
			for (TeamDoublePair gs : scores.get(team)) {
				if (gs.scoreMargin > 0) {
					gs.scoreMargin = Math.log(gs.scoreMargin + 1) / totalPoints;
				} else {
					gs.scoreMargin = 0.0;
				}
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Running score parser");
			GameRanker sp = new GameRanker(new File("data/2009_pbp"));
			
			TeamDoublePair[] ranks = sp.computeRank();
			for (TeamDoublePair p : ranks) {
				System.out.println(p.otherTeam + ": " + -p.scoreMargin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
