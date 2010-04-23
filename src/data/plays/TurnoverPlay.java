package data.plays;

import data.GameState;
import data.Player;

public class TurnoverPlay extends Play {
	
	protected Player handler, stealer;
	protected String reason;
	
	public TurnoverPlay(String id, int time, Player[] active, Player handler, String reason, Player stealer) {
		super(id, time, active);
		
		this.handler = handler;
		this.reason = reason;
		this.stealer = stealer;
	}

	@Override
	protected void apply(GameState g) {
		g.turnover(handler, reason, stealer);
	}
}
