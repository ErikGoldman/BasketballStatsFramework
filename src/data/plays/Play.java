package data.plays;

import data.Game;
import data.GameState;
import data.Player;

public abstract class Play {
	
	protected Game game;
	protected int time;
	protected Player[] activePlayers;
	
	public Game getGame() {
		return game;
	}
	
	protected Play(String id, int time, Player[] players) {
		game = Game.getGame(id);
		this.time = time;
		this.activePlayers = players;
	}
	
	@Override
	public String toString() {
		return game + "|" + time;
	}
	
	protected abstract void apply(GameState g);
	
	public void applyToGame(GameState g) {
		g.updateTime(activePlayers, time);
		apply(g);
	}
}
