package data.plays;

import data.GameState;
import data.Player;


public class SubPlay extends Play {
	Player inPlayer, outPlayer;
	
	public SubPlay(String id, int time, Player[] players, Player inPlayer, Player outPlayer) {
		super(id, time, players);
		
		this.inPlayer = inPlayer;
		this.outPlayer = outPlayer;
	}

	@Override
	protected void apply(GameState g) {
		g.substitution(outPlayer, inPlayer);
	}

}
