package data.plays;

import data.GameState;
import data.Player;

public class EjectionPlay extends Play {

	protected Player ejected;
	protected String reason;
	
	public EjectionPlay(String id, int time, Player[] players, Player ejected, String reason) {
		super(id, time, players);
		
		this.ejected = ejected;
		this.reason = reason;
	}

	@Override
	protected void apply(GameState g) {
		g.ejection(ejected, reason);
	}

}
