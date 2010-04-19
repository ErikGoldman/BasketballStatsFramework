package data;

public class EndOfPeriodPlay extends Play {
	
	public EndOfPeriodPlay(String id, int time, Player[] players) {
		super(id, time, players);
	}

	@Override
	protected void apply(GameState g) {
		g.updateTime(activePlayers, time);
	}
}
