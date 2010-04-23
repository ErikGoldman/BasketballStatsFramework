package example;

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
	public GameState createCopy(Game g) {
		return new PMCalc(g, data);
	}
	
	@Override
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {
		// every time we advance the clock, update the set of players who were active
		// during that time.
		data.tick(players, newTime - oldTime);
	}
	
	@Override
	protected void handleScoreChange(String team, Player[] players, int scoreDelta) {
		if (game.isAway(team)) {
			data.tickPoints(players, 0, scoreDelta);
			data.tickPoints(players, 5, -scoreDelta);
		}
		else {
			data.tickPoints(players, 5, scoreDelta);
			data.tickPoints(players, 0, -scoreDelta);
		}
	}
}
