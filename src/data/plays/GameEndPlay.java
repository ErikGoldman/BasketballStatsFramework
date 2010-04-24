package data.plays;

import data.GameState;
import data.Player;

public class GameEndPlay extends Play {

	public GameEndPlay(String id, int time, Player[] players) {
		super(id, time, players);
	}

	@Override
	protected void apply(GameState g) {
		g.gameEnd();
	}

}
