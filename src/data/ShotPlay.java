package data;

import PlusMinus.Player;

public class ShotPlay extends Play {
	
	protected Player scorer;
	protected String team;
	protected int newScore;
	protected boolean made;
	
	public ShotPlay(String id, String lns, String time, String team, Player player, int newScore, boolean made) {
		super(id, lns, time);
		
		this.team = team;
		this.scorer = player;
		this.newScore = newScore;
		this.made = made;
	}

	@Override
	public void applyToGame(GameState g) {
		if (this.made) {
			// TODO: do player stats
			g.updateScore(team, newScore);
		}
	}

}
