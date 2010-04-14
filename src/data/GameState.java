package data;

public class GameState {
	private Game game;
	
	private int homeScore = 0, awayScore = 0;
	
	public GameState(Game game) {
		this.game = game;
	}
	
	public void updateScore(String team, int score) {
		if (game.isHome(team)) {
			homeScore = score;
		} else {
			awayScore = score;
		}
	}
	
	public int getHomeScore() {
		return homeScore;
	}
	
	public int getAwayScore() {
		return awayScore;
	}
	
	public Game getGame() {
		return game;
	}
}
