package data;

/**
 * The main class you will deal with in order to use this framework.  The goal
 * is to subclass this and override the methods of any event that you want to
 * trap.
 * 
 * IMPORTANT NOTE: you MUST override createCopy in order for almost
 * anything to work!  createCopy just generates a new instance of this
 * class with any important shared data passed into it.  See the example
 * roster calculation code in the tools package to see how this works.
 *
 */
public class GameState {
	protected Game game;
	protected int currentTime = 0, currentPeriod = 0;
	
	protected Player[] activePlayers;
	
	protected int homeScore = 0, awayScore = 0;

	public GameState(Game game) {
		this.game = game;
	}
	
	public GameState createCopy(Game g) {
		return new GameState(g);
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
		if (activePlayers != null)
			handleTimeChange(activePlayers, currentTime, newTime);
		
		activePlayers = players;
		currentTime = newTime;
	}
	
	public void updateScore(String team, Player[] players, int scoreDelta) {
		handleScoreChange(team, players, scoreDelta);
		
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
	public void turnover(Player handler, String reason, Player stealer) {}
	public void timeout(String team, String type) {}
	public void violation(String team, String type) {}
	public void ejection(Player ejected, String reason) {}
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {}
	protected void handleScoreChange(String team, Player[] players, int scoreDelta) {}
}
