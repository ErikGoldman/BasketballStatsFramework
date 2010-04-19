package parser;

import data.Game;
import data.GameState;
import data.Player;

public class RosterCalcState extends GameState {
	
	RosterCalculator rc;
	
	public RosterCalcState(Game g, RosterCalculator rc) {
		super(g);
		
		this.rc = rc;
	}

	@Override
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {
		for (Player p : players)
			rc.addPlayer(p);
	}
}
