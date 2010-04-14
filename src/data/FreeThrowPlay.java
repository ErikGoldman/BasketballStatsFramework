package data;

import PlusMinus.Player;

public class FreeThrowPlay extends ShotPlay {
	
	
	public FreeThrowPlay(String id, String lns, String time, String team, Player player, int newScore, boolean made) {
		super(id, lns, time, team, player, newScore, made);		
	}

	@Override
	public void applyToGame(GameState g) {
		g.updateScore(team, newScore);
	}
}
