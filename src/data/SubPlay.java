package data;

import PlusMinus.Player;

public class SubPlay extends Play {
	
	public SubPlay(String id, String lns, String time, String team, Player inPlayer, Player outPlayer) {
		super(id, lns, time);
	}

	@Override
	public void applyToGame(GameState g) {
		// TODO: do this correctly
	}

}
