package data.plays;

import data.GameState;
import data.Player;

public class ViolationPlay extends Play {

	private String team, type;
	public ViolationPlay(String id, int time, Player[] players, String team, String type) {
		super(id, time, players);
		
		this.team = team;
		this.type = type;
	}
	
	@Override
	protected void apply(GameState g) {
		g.violation(team, type);
	}

}
