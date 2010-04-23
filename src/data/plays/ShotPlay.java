package data.plays;

import data.GameState;
import data.Player;


public class ShotPlay extends Play {
	
	protected Player scorer, assist;
	protected String type;
	protected int points, x, y;
	protected boolean made;
	
	public ShotPlay(String id, int time, Player[] players, Player player, String type, int points, boolean made, Player assist, int x, int y) {
		super(id, time, players);
		
		this.type = type;
		this.scorer = player;
		this.points = points;
		this.made = made;
		this.x = x;
		this.y = y;
		this.assist = assist;
	}

	@Override
	protected void apply(GameState g) {
		if (this.made) {
			g.updateScore(scorer.getTeam(), activePlayers, points);
		}
		
		// individual stats
		g.shotTaken(scorer, type, points, made);
	}

}
