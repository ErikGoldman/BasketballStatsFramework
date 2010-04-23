package parser;

import data.Game;
import data.GameState;
import data.Player;

public class RosterCalcState extends GameState {
	
	Roster rc;
	
	public RosterCalcState(Game g, Roster rc) {
		super(g);
		
		this.rc = rc;
	}
	
	@Override
	public GameState createCopy(Game g) {
		return new RosterCalcState(g, rc);
	}

	@Override
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {
		for (Player p : players)
			rc.addPlayer(p);
	}
}
