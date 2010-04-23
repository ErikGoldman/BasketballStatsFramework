package data.plays;

import data.GameState;
import data.Player;

public class TimeoutPlay extends Play {
	private static final String OFFICIAL_TYPE = "official";
	
	private String team, type;
	public TimeoutPlay(String id, int time, Player[] players, String team, String type) {
		super(id, time, players);
		
		if (type.equals(OFFICIAL_TYPE)) {
			team = null;
		}
		
		this.team = team;
		this.type = type;
	}

	@Override
	protected void apply(GameState g) {
		g.timeout(team, type);
	}

}
