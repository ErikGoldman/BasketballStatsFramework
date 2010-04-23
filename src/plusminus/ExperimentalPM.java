package plusminus;

import data.Game;
import data.GameState;
import data.Player;

public class ExperimentalPM extends GameState {
	ExperimentalPMData data;
	
	public ExperimentalPM(Game game, ExperimentalPMData d) {
		super(game);
		this.data = d;
	}
	
	@Override
	public GameState createCopy(Game g) {
		return new ExperimentalPM(g, data);
	}
	
	@Override
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {
		data.tick(players, newTime - oldTime);
	}
	
	@Override
	protected void handleScoreChange(String team, Player[] players, int scoreDelta) {
		if (game.isAway(team)) {
			data.tickPoints(players, 0, scoreDelta);
		}
		else {
			data.tickPoints(players, 5, scoreDelta);
		}
	}
}
