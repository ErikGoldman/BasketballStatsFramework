package data;

public class GameState {
	protected Game game;
	protected int currentTime = 0, currentPeriod = 0;
	
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
	
	public void updateTime(Player[] players, int newTime) {
		handleTimeChange(players, currentTime, newTime);
		currentTime = newTime;
	}
	
	public void updateScore(String team, int scoreDelta) {
		handleScoreChange(team, scoreDelta);
		
		if (game.isHome(team))
			homeScore += scoreDelta;
		else
			awayScore += scoreDelta;
	}
	
	public void shotTaken(Player p, String type, int points, boolean made) {}
	public void freeThrow(Player p, int num, boolean made) {}
	public void substitution(Player out, Player in) {}
	public void rebound(Player out, boolean isOffensive) {}
	public void foul(Player fouler, Player fouled, String type) {}
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {}
	protected void handleScoreChange(String team, int newScore) {}
}
