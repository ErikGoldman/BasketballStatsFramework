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
		if (!this.getClass().equals(GameState.class)) {
			throw new RuntimeException("YOU MUST OVERRIDE createCopy TO HAVE A VALID SUBCLASS!");
		}
		
		return new GameState(g);
	}
	
	// these are all the things you can override
	public void shotTaken(Player p, String type, int points, boolean made) {}
	public void freeThrow(Player p, int num, boolean made) {}
	public void substitution(Player out, Player in) {}
	public void rebound(Player out, boolean isOffensive) {}
	public void foul(Player fouler, Player fouled, String type) {}
	public void turnover(Player handler, String reason, Player stealer) {}
	public void timeout(String team, String type) {}
	public void violation(String team, String type) {}
	public void ejection(Player ejected, String reason) {}
	
	/*
	 * This will fire every play, representing the passage of time "between" 
	 * events in our system.  Override this to write your own handler.
	 * 
	 * @param players represents the players who spend this time difference on the court
	 * @param oldTime the game time in seconds (starting from 0 at the jump ball) of the last play
	 * @param newTime the current game time
	 */
	protected void handleTimeChange(Player[] players, int oldTime, int newTime) {}
	protected void handleScoreChange(String team, Player[] players, int scoreDelta) {}
	
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
}
