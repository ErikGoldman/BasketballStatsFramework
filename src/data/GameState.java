package data;

public class GameState {
	protected Game game;
	protected int currentTime = 0;
	
	protected int homeScore = 0, awayScore = 0;

	public GameState(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public String toString() {
		return currentTime + ":    " + game.getHomeTeam() + ": " + homeScore + "   " + game.getAwayTeam() + "  " + awayScore;
	}
	
	public int getHomeScore() {
		return homeScore;
	}
	public int getAwayScore() {
		return awayScore;
	}
	public int getScore(String team) {
		if (game.isHome(team))
			return homeScore;
		if (game.isAway(team))
			return awayScore;
		
		throw new RuntimeException("Team " + team + " is not valid for the game " + game);
	}
	
	public void updateTime(int newTime) {
		handleTimeChange(currentTime, newTime);
		currentTime = newTime;
	}
	
	public void updateScore(String team, int newScore) {
		handleScoreChange(team, newScore);
		
		if (game.isHome(team))
			homeScore = newScore;
		else
			awayScore = newScore;
	}
	
	public void shotTaken(Player p, String type, int points, boolean made) {}
	public void freeThrow(Player p, boolean made) {}
	public void substitution(Player out, Player in) {}
	public void rebound(Player out, int off, int def) {}
	protected void handleTimeChange(int oldTime, int newTime) {}
	protected void handleScoreChange(String team, int newScore) {}
}
