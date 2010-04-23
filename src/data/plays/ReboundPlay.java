package data.plays;

import data.GameState;
import data.Player;

public class ReboundPlay extends Play {
	
	private Player player;
	private boolean isOffensive;
	
	public ReboundPlay(String id, int time, Player[] players, Player player, boolean isOffensive) {
		super(id, time, players);
		
		this.player = player;
		this.isOffensive = isOffensive;
	}

	@Override
	protected void apply(GameState g) {
		g.rebound(player, isOffensive);
	}

}
