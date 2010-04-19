package PlusMinus;

import data.Game;
import data.GameState;
import data.Player;

public class PMCalc extends GameState {
	PMData data;
	
	public PMCalc(Game game, PMData d) {
		super(game);
		this.data = d;
	}
	
	@Override
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {
		data.tick(players, newTime - oldTime);
	}
}
