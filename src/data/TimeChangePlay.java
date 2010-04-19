package data;

public class TimeChangePlay extends Play {
	
	public TimeChangePlay(String id, int time, Player[] players) {
		super(id, time, players);
	}

	@Override
	protected void apply(GameState g) {
		g.updateTime(activePlayers, time);
	}
}
