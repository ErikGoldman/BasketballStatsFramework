import java.io.File;

import parser.PbpFile;
import data.GameState;


public class Main {
	public static void main(String[] args) {
		try {
			PbpFile game = new PbpFile(new File("data/2008_pbp/20081028CLEBOS"));
			
			System.out.println(game);
			
			GameState gs = game.getNewState();
			for (int i=0; i < game.getNumPlays(); i++) {
				game.apply(i, gs);
			}
			
			System.out.println("Final score: " + gs.getGame().getHomeTeam() + " " + gs.getHomeScore()
						+ " " + gs.getGame().getAwayTeam() + " " + gs.getAwayScore());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
